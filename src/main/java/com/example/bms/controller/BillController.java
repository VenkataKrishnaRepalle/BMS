package com.example.bms.controller;

import com.example.bms.dto.AddBillRequestDto;
import com.example.bms.dto.BillResponseDto;
import com.example.bms.entity.BillExtra;
import com.example.bms.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("/add-bill")
    public ResponseEntity<BillResponseDto> addBill(@RequestBody AddBillRequestDto billRequestDto) {
        return new ResponseEntity<>(billService.addBill(billRequestDto), CREATED);
    }

    @GetMapping("/view-bill/{billId}")
    public ResponseEntity<BillResponseDto> viewBill(@PathVariable UUID billId) {
        return new ResponseEntity<>(billService.viewBill(billId), OK);
    }

    @PostMapping("/add-charges/{billId}")
    public ResponseEntity<BillResponseDto> addCharges(@PathVariable UUID billId, @RequestBody List<BillExtra> billExtraDto) {
        return new ResponseEntity<>(billService.addCharges(billId, billExtraDto), CREATED);
    }

    @PostMapping("/edit-charge/bill/{billId}/billExtra/{billExtraId}")
    public ResponseEntity<BillResponseDto> editCharges(@PathVariable UUID billId,
                                                       @PathVariable UUID billExtraId,
                                                       @RequestBody BillExtra billExtraDto) {
        return new ResponseEntity<>(billService.editCharges(billId, billExtraId, billExtraDto), OK);
    }

    @DeleteMapping("/delete/{billId}")
    public ResponseEntity<HttpStatus> deleteBillById(@PathVariable UUID billId) {
        billService.deleteBillById(billId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
