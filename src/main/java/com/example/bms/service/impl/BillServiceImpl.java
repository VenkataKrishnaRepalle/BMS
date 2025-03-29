package com.example.bms.service.impl;

import com.example.bms.dao.BillDao;
import com.example.bms.dao.BillExtraDao;
import com.example.bms.dao.ResultDao;
import com.example.bms.dto.AddBillRequestDto;
import com.example.bms.dto.BillResponseDto;
import com.example.bms.entity.Bill;
import com.example.bms.entity.BillExtra;
import com.example.bms.entity.Result;
import com.example.bms.exception.IntegrityException;
import com.example.bms.exception.InvalidInputException;
import com.example.bms.exception.NotFoundException;
import com.example.bms.service.BillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.bms.entity.BillExtraOperation.ADDITION;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillServiceImpl implements BillService {

    private final BillDao billDao;

    private final BillExtraDao billExtraDao;

    private final ResultDao resultDao;

    private static final String RESULT_NOT_FOUND = "RESULT_NOT_FOUND";

    private static final String RESULT_NOT_FOUND_MESSAGE = "Result not found for bill: ";

    @Override
    @Transactional
    public BillResponseDto addBill(AddBillRequestDto billRequestDto) {
        if (billRequestDto.getBill() == null) {
            throw new InvalidInputException("INVALID_INPUT_EXCEPTION", "Bill is empty, please re-submit again");
        }

        var bill = insertBill(billRequestDto.getBill());
        log.info("BillServiceImpl - Add Bill : Bill saved - {}", bill);

        var billExtras = billRequestDto.getBillExtras();
        List<BillExtra> savedBillExtras = new ArrayList<>();
        if (billExtras != null) {
            for (var billExtra : billExtras) {
                billExtra.setUuid(UUID.randomUUID());
                billExtra.setBillUuid(bill.getUuid());
                savedBillExtras.add(insertBillExtra(billExtra));
                log.info("BillServiceImpl - Add Bill : Bill extra saved - {}", billExtra);
            }
        }
        var result = deductBagsAndKgs(bill);
        log.info("BillServiceImpl - Add Bill : result saved - {}", result);

        return BillResponseDto.builder()
                .bill(bill)
                .billExtras(savedBillExtras)
                .result(result)
                .build();
    }

    private Bill insertBill(Bill bill) {
        bill.setUuid(UUID.randomUUID());
        bill.setCreatedTime(Instant.now());
        bill.setUpdatedTime(Instant.now());
        try {
            if (0 == billDao.insert(bill)) {
                throw new IntegrityException("BILL_NOT_INSERTED", "Bill not created");
            }
        } catch (DataIntegrityViolationException exception) {
            throw new IntegrityException("BILL_NOT_INSERTED", exception.getCause().getMessage());
        }
        return bill;
    }

    private BillExtra insertBillExtra(BillExtra billExtra) {
        billExtra.setCreatedTime(Instant.now());
        billExtra.setUpdatedTime(Instant.now());
        try {
            if (0 == billExtraDao.insert(billExtra)) {
                throw new IntegrityException("BILL_EXTRA_NOT_INSERTED", "Bill Extra not inserted");
            }
        } catch (DataIntegrityViolationException exception) {
            throw new IntegrityException("BILL_EXTRA_NOT_INSERTED", exception.getCause().getMessage());
        }
        return billExtra;
    }

    @Override
    public BillResponseDto viewBill(UUID billId) {
        var bill = get(billId);
        var billExtra = billExtraDao.getByBillId(billId);
        var result = resultDao.getByBillId(billId);
        return BillResponseDto.builder()
                .bill(bill)
                .billExtras(billExtra)
                .result(result)
                .build();
    }

    @Override
    @Transactional
    public BillResponseDto addCharges(UUID billId, List<BillExtra> billExtraDto) {
        var bill = get(billId);
        var result = resultDao.getByBillId(billId);
        if (billExtraDto != null) {
            for (var billExtra : billExtraDto) {
                billExtra.setUuid(UUID.randomUUID());
                billExtra.setBillUuid(billId);
                billExtra.setCreatedTime(Instant.now());
                billExtra.setUpdatedTime(Instant.now());
                var savedBillExtra = insertBillExtra(billExtra);
                log.info("BillServiceImpl - Add Charges : Bill extra saved - {}", savedBillExtra);
            }
            result.setTotal(calculateOverallBillAmount(bill.getUuid()));
            result.setUpdatedTime(Instant.now());
            updateResult(result);
            log.info("BillServiceImpl - Add Charges : result updated - {}", result);
        }

        return BillResponseDto.builder()
                .bill(bill)
                .billExtras(billExtraDao.getByBillId(bill.getUuid()))
                .result(result)
                .build();
    }

    private void updateResult(Result result) {
        try {
            if (0 == resultDao.update(result)) {
                throw new IntegrityException("RESULT_NOT_UPDATED", "Result not updated with id: " + result.getUuid());
            }
        } catch (DataIntegrityViolationException exception) {
            throw new IntegrityException("RESULT_NOT_UPDATED", exception.getCause().getMessage());
        }
    }

    @Override
    @Transactional
    public BillResponseDto editCharges(UUID billId, UUID billExtraId, BillExtra billExtraDto) {
        if (!billId.equals(billExtraDto.getBillUuid()) || !billExtraId.equals(billExtraDto.getUuid())) {
            throw new InvalidInputException("INVALID_INPUT", "Invalid input of billId and Bill Extra");
        }
        var bill = get(billId);
        getBillExtraById(billExtraDto.getUuid());
        var result = getResultByBillId(billId);

        updateBillExtra(billExtraDto);
        log.info("BillServiceImpl - Edit Charges : Bill extra updated - {}", billExtraDto);

        result.setTotal(calculateOverallBillAmount(billId));
        result.setUpdatedTime(Instant.now());
        updateResult(result);
        log.info("BillServiceImpl - Edit Charges : result saved - {}", result);

        var billExtraList = billExtraDao.getByBillId(billId);

        return BillResponseDto.builder()
                .bill(bill)
                .billExtras(billExtraList)
                .result(result)
                .build();
    }

    private void updateBillExtra(BillExtra billExtra) {
        try {
            if (0 == billExtraDao.update(billExtra)) {
                throw new IntegrityException("BILL_EXTRA_NOT_UPDATED",
                        "Bill extra not updated for id: " + billExtra.getUuid());
            }
        } catch (DataIntegrityViolationException exception) {
            throw new IntegrityException("BILL_EXTRA_NOT_UPDATED", exception.getCause().getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteBillById(UUID billId) {
        get(billId);
        try {
            if (0 == billDao.delete(billId)) {
                throw new IntegrityException("BILL_NOT_DELETED", "Bill not deleted with id: " + billId);
            }
        } catch (DataIntegrityViolationException exception) {
            throw new IntegrityException("BILL_NOT_DELETED", exception.getCause().getMessage());
        }
    }

    private Result deductBagsAndKgs(Bill bill) {
        var result = resultDao.getByBillId(bill.getUuid());
        var totalKgs = (bill.getBags() * 75) + bill.getKgs();
        var afterDeduction = totalKgs - (bill.getBags() * 5);
        var totalBagsAfterDeductions = afterDeduction / 75;
        var totalKgsAfterDeductions = afterDeduction - (totalBagsAfterDeductions * 75);

        var bagsCost = bill.getPrice()
                .multiply(BigDecimal.valueOf(totalBagsAfterDeductions));
        var kgsCost = bill.getPrice()
                .divide(BigDecimal.valueOf(75), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(totalKgsAfterDeductions));

        var finalAmount = bagsCost.add(kgsCost);
        var newResult = Result.builder()
                .billUuid(bill.getUuid())
                .bags(totalBagsAfterDeductions)
                .kgs(totalKgsAfterDeductions)
                .bagsAmount(bagsCost)
                .kgsAmount(kgsCost)
                .finalAmount(finalAmount)
                .total(finalAmount)
                .createdTime(Instant.now())
                .updatedTime(Instant.now())
                .build();
        if (result == null) {
            newResult.setUuid(UUID.randomUUID());
            resultDao.insert(newResult);
        }
        newResult.setTotal(calculateOverallBillAmount(bill.getUuid()));
        updateResult(newResult);
        return newResult;
    }

    private BigDecimal calculateOverallBillAmount(UUID billUuid) {
        get(billUuid);
        var result = resultDao.getByBillId(billUuid);
        var total = Optional.ofNullable(result)
                .map(Result::getFinalAmount)
                .orElse(BigDecimal.ZERO);
        System.out.println(total);
        var billExtras = billExtraDao.getByBillId(billUuid);
        for (var billExtra : billExtras) {
            total = billExtra.getOperation() == ADDITION ? total.add(billExtra.getAmount()) :
                    total.subtract(billExtra.getAmount());
        }
        System.out.println(total);
        return total;
    }

    private Bill get(UUID billId) {
        var bill = billDao.getById(billId);
        if (bill == null) {
            throw new NotFoundException("BILL_NOT_FOUND", "Bill not found with id: " + billId);
        }
        return bill;
    }

    private void getBillExtraById(UUID billExtraUuid) {
        var billExtra = billExtraDao.getById(billExtraUuid);
        if (billExtra == null) {
            throw new NotFoundException("BILL_EXTRA_NOT_FOUND", "Bill extra not found with id: " + billExtraUuid);
        }
    }

    private Result getResultByBillId(UUID billUuid) {
        var result = resultDao.getByBillId(billUuid);
        if (result == null) {
            throw new NotFoundException(RESULT_NOT_FOUND, RESULT_NOT_FOUND_MESSAGE + billUuid);
        }
        return result;
    }
}