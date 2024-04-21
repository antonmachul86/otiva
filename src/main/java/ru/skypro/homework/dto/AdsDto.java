package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
public class AdsDto {
   private Integer count;
   private List<AdDto> results;

   public AdsDto(List<AdDto> results){
       this.results=results;
       this.count=results.size();
   }
}
