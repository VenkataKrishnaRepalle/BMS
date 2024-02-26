package com.example.bms.mapper;

import com.example.bms.dto.*;
import com.example.bms.entity.Bill;
import com.example.bms.entity.BillExtra;
import com.example.bms.entity.Result;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface BillMapper {
    Bill addBillRequestDtoToBill(AddBillRequestDto addBillRequestDto);

    BillExtra addBillExtraRequestDtoToBillExtra(AddBillExtraRequestDto addBillExtraRequestDto);

    List<BillExtra> addBillExtraRequestDtoListTOBillExtraList(List<AddBillExtraRequestDto> addBillExtraRequestDtoList);

    List<AddBillExtraResponseDto> billExtraListToAddBillExtraResponseDtoList(List<BillExtra> billExtrasList);

    AddBillResultResponseDto resultTOAddBillResponseDto(Result result);

    BillResponseDto billToAddBillResponseDto(Bill bill);
}
