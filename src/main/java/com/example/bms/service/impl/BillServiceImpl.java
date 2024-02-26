package com.example.bms.service.impl;

import com.example.bms.dto.AddBillExtraRequestDto;
import com.example.bms.dto.AddBillRequestDto;
import com.example.bms.dto.BillResponseDto;
import com.example.bms.entity.Bill;
import com.example.bms.entity.Result;
import com.example.bms.mapper.BillMapper;
import com.example.bms.repository.BillExtraRepository;
import com.example.bms.repository.BillRepository;
import com.example.bms.repository.ResultRepository;
import com.example.bms.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.example.bms.entity.BillExtraStatus.ADDITION;
import static com.example.bms.entity.BillExtraStatus.DEDUCTION;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillMapper billMapper;

    private final BillRepository billRepository;

    private final BillExtraRepository billExtraRepository;

    private final ResultRepository resultRepository;

    @Override
    public BillResponseDto addBill(AddBillRequestDto addBillRequestDto) {
        var bill = billMapper.addBillRequestDtoToBill(addBillRequestDto);
        var billExtra = billMapper.addBillExtraRequestDtoListTOBillExtraList(addBillRequestDto.getAdditionalBills());

        var result = deductBagsAndKgs(bill);

        bill = billRepository.save(bill);
        if (billExtra != null) {
            for (var eachBill : billExtra) {
                if (ADDITION.equals(eachBill.getStatus())) {
                    result.setFinalAmount(result.getFinalAmount().add(eachBill.getAmount()));
                    eachBill.setBill(bill);
                } else if (DEDUCTION.equals(eachBill.getStatus())) {
                    result.setFinalAmount(result.getFinalAmount().subtract(eachBill.getAmount()));
                    eachBill.setBill(bill);
                }
            }
            billExtra = billExtraRepository.saveAll(billExtra);
        }
        result = resultRepository.save(result);

        var response = billMapper.billToAddBillResponseDto(bill);
        response.setAdditionalBills(billMapper.billExtraListToAddBillExtraResponseDtoList(billExtra));
        response.setResult(billMapper.resultTOAddBillResponseDto(result));

        return response;
    }

    @Override
    public BillResponseDto viewBill(UUID billId) {
        var bill = billRepository.getReferenceById(billId);
        var billExtra = TRUE.equals(billExtraRepository.existsByBill_Uuid(billId)) ? billExtraRepository.getAllByBill_Uuid(billId) : null;
        var result = TRUE.equals(resultRepository.existsByBill_Uuid(billId)) ? resultRepository.findByBill_Uuid(billId) : null;

        var response = billMapper.billToAddBillResponseDto(bill);
        response.setAdditionalBills(billMapper.billExtraListToAddBillExtraResponseDtoList(billExtra));
        response.setResult(billMapper.resultTOAddBillResponseDto(result));

        return response;
    }

    @Override
    public BillResponseDto addCharges(UUID billId, List<AddBillExtraRequestDto> billExtraRequestDto) {
        var bill = billRepository.getReferenceById(billId);
        var billExtraRequest = billMapper.addBillExtraRequestDtoListTOBillExtraList(billExtraRequestDto);
        var billResult = TRUE.equals(resultRepository.existsByBill_Uuid(billId)) ? resultRepository.findByBill_Uuid(billId) : null;

        for (var billExtra : billExtraRequest) {
            if (ADDITION.equals(billExtra.getStatus())) {
                billResult.setFinalAmount(billResult.getFinalAmount().add(billExtra.getAmount()));
            } else if (DEDUCTION.equals(billExtra.getStatus())) {
                billResult.setFinalAmount(billResult.getFinalAmount().subtract(billExtra.getAmount()));
            }
        }
        billExtraRepository.saveAll(billExtraRequest);
        resultRepository.save(billResult);

        var response = billMapper.billToAddBillResponseDto(bill);
        response.setAdditionalBills(billMapper.billExtraListToAddBillExtraResponseDtoList(billExtraRequest));
        response.setResult(billMapper.resultTOAddBillResponseDto(billResult));

        return response;
    }

    @Override
    public BillResponseDto editCharges(UUID billId, UUID billExtraId, AddBillExtraRequestDto addBillExtraRequestDto) {
        var bill = billRepository.getReferenceById(billId);
        var billExtra = billExtraRepository.getReferenceById(billExtraId);
        var result = resultRepository.existsByBill_Uuid(billId) ? resultRepository.findByBill_Uuid(billId) : null;

        if (bill != null && result != null && billExtra != null) {
            if (ADDITION.equals(billExtra.getStatus())) {
                result.setFinalAmount(result.getFinalAmount().subtract(billExtra.getAmount()));
            } else if (DEDUCTION.equals(billExtra.getStatus())) {
                result.setFinalAmount(result.getFinalAmount().add(billExtra.getAmount()));
            }
        }

        billExtra.setName(addBillExtraRequestDto.getName());
        billExtra.setAmount(addBillExtraRequestDto.getAmount());
        billExtra.setStatus(addBillExtraRequestDto.getStatus());

        var amount = ADDITION.equals(billExtra.getStatus()) ? result.getFinalAmount().add(billExtra.getAmount()) :
                result.getFinalAmount().subtract(billExtra.getAmount());
        result.setFinalAmount(amount);

        billExtraRepository.save(billExtra);
        resultRepository.save(result);

        var billExtraList = billExtraRepository.existsByBill_Uuid(billId) ? billExtraRepository.getAllByBill_Uuid(billId) : null;

        var response = billMapper.billToAddBillResponseDto(bill);
        response.setAdditionalBills(billMapper.billExtraListToAddBillExtraResponseDtoList(billExtraList));
        response.setResult(billMapper.resultTOAddBillResponseDto(result));

        return response;
    }

    private Result deductBagsAndKgs(Bill bill) {
        var totalKgs = (bill.getBags() * 75) + bill.getKgs();
        var afterDeduction = totalKgs - (bill.getBags() * 5);
        var totalBagsAfterDeductions = afterDeduction / 75;
        var totalKgsAfterDeductions = afterDeduction - (totalBagsAfterDeductions * 75);

        var bagsCost = bill.getPrice().multiply(BigDecimal.valueOf(totalBagsAfterDeductions));
        var kgsCost = BigDecimal.valueOf(75).divide(bill.getPrice()).multiply(BigDecimal.valueOf(totalKgsAfterDeductions));

        var finalAmount = bagsCost.add(kgsCost);

        return Result.builder().bags(totalBagsAfterDeductions).kgs(totalKgsAfterDeductions).bagsAmount(bagsCost).kgsAmount(kgsCost).finalAmount(finalAmount).bill(bill).build();
    }
}