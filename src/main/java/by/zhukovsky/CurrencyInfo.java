package by.zhukovsky;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyInfo {
    @JsonProperty("Cur_Abbreviation")
    private String code;
    @JsonProperty("Date")
    private Date date;
    @JsonProperty("Cur_OfficialRate")
    private BigDecimal rate;
    @JsonProperty("Cur_Scale")
    private Integer scale;

    public CurrencyInfo() {
    }

    public CurrencyInfo(String code, Date date, BigDecimal rate, Integer scale) {
        this.code = code;
        this.date = date;
        this.rate = rate;
        this.scale = scale;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }
}
