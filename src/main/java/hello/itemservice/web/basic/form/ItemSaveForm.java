package hello.itemservice.web.basic.form;

import hello.itemservice.domain.Item.ItemType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
public class ItemSaveForm {
    @NotBlank
    private String itemName;

    @NotNull
    @Range(min=1000,max=1000000)
    private Integer price;

    @NotNull
    @Max(value = 9999)
    private Integer quantity;

    private Boolean open; // 판매 여부
    private List<String> regions; // 등록지역
    private ItemType itemType;
    private String deliveryCode;



}
