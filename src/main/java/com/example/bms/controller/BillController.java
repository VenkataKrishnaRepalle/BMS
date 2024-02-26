package com.example.bms.controller;

import com.example.bms.dto.AddBillExtraRequestDto;
import com.example.bms.dto.AddBillRequestDto;
import com.example.bms.dto.BillResponseDto;
import com.example.bms.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("/add-bill")
    public ResponseEntity<BillResponseDto> addBill(@RequestBody AddBillRequestDto addBillRequestDto) {
        return new ResponseEntity<>(billService.addBill(addBillRequestDto), CREATED);
    }

    @GetMapping("/view-bill/{billId}")
    public ResponseEntity<BillResponseDto> viewBill(@PathVariable UUID billId) {
        return new ResponseEntity<>(billService.viewBill(billId), OK);
    }

    @PostMapping("/add-charges/{billId}")
    public ResponseEntity<BillResponseDto> addCharges(@PathVariable UUID billId, @RequestBody List<AddBillExtraRequestDto> billExtraRequestDto) {
        return new ResponseEntity<>(billService.addCharges(billId, billExtraRequestDto), CREATED);
    }

    @PostMapping("/edit-charge/bill/{billId}/billExtra/{billExtraId}")
    public ResponseEntity<BillResponseDto> editCharges(@PathVariable UUID billId,
                                                       @PathVariable UUID billExtraId,
                                                       @RequestBody AddBillExtraRequestDto addBillExtraRequestDto) {
        return new ResponseEntity<>(billService.editCharges(billId, billExtraId, addBillExtraRequestDto), OK);
    }
}
