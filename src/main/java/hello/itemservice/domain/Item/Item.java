package hello.itemservice.domain.Item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
public class Item {
    //@NotNull(groups = UpdatedCheck.class)
    private Long id;
    //@NotBlank(groups = {SaveCheck.class, UpdatedCheck.class})
    private String itemName;

    //@NotNull(groups = {SaveCheck.class, UpdatedCheck.class})
    //@Range(min=1000,max=1000000,groups = {SaveCheck.class, UpdatedCheck.class})
    private Integer price;

//    @NotNull(groups = {SaveCheck.class, UpdatedCheck.class})
//    @Max(value = 9999, groups = SaveCheck.class)
    private Integer quantity;

    private Boolean open; // 판매 여부
    private List<String> regions; // 등록지역
    private ItemType itemType;
    private String deliveryCode;


    public Item() {
    }
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
    public void setSaveForm(String itemName, Integer price, Integer quantity, Boolean open, List<String> regions, ItemType itemType, String deliveryCode){
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.open = open;
        this.regions = regions;
        this.itemType = itemType;
        this.deliveryCode =deliveryCode;
    }
    public void setUpdateForm(Long id, String itemName, Integer price, Integer quantity, Boolean open, List<String> regions, ItemType itemType, String deliveryCode){
        this.id=id;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.open = open;
        this.regions = regions;
        this.itemType = itemType;
        this.deliveryCode =deliveryCode;
    }
}
