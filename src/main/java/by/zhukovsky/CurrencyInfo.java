package by.zhukovsky;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyInfo {
    @JsonProperty("Cur_Abbreviation")
    private String code;
    @JsonProperty("Date")
    private Date date;
    @JsonProperty("Cur_OfficialRate")
    private BigDecimal rate;
    @JsonProperty("Cur_Scale")
    private Integer scale;
}
