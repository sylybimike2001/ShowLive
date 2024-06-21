package com.ayb.service;

import com.ayb.entity.DTO.Result;
import com.ayb.entity.Show;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecommendService {

    Result pageSelectionByType(Integer typeId, Integer current, Double x, Double y);
    List<Show> recommendByType(Integer typeId, Integer quantity, Double x, Double y);
    Result autoFillRecommend( Integer typeId, Integer quantity, Double x, Double y);
}
