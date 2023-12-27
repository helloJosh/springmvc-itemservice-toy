package hello.itemservice.web.basic;

import hello.itemservice.domain.Item.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/v3/items")
@RequiredArgsConstructor
public class BasicItemControllerV3 {
    private final ItemRepository itemRepository;

    @ModelAttribute("regions")
    public static Map<String, String> regions(){
        Map<String, String> regions = new LinkedHashMap<>();

        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");

        return regions;
    }
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes(){
        return ItemType.values();
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes(){
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable(name = "itemId") long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("item", new Item());
        return "form/v3/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "form/v3/addform";
        }

        Item savedItem = itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        //model.addAttribute("item", item); 자동추가 생략가능

        return "redirect:/form/v3/items/{itemId}";
    }
    @PostMapping("/add")
    public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "form/v3/addform";
        }

        Item savedItem = itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        //model.addAttribute("item", item); 자동추가 생략가능

        return "redirect:/form/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable(name = "itemId") Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/v3/editForm";
    }
    //@PostMapping("/{itemId}/edit")
    public String edit(@PathVariable(name = "itemId") Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult){
        //특정 필드가 아닌 복합룰 검증
        if(item.getPrice() != null && item.getQuantity()!= null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,resultPrice}, null);
            }
        }
        if(bindingResult.hasErrors()){
            log.info("errors= {}", bindingResult);
            return "form/v3/editForm";
        }

        itemRepository.update(itemId,item);
        return "redirect:/form/v3/items/{itemId}";
    }
    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable(name = "itemId") Long itemId, @Validated(UpdatedCheck.class) @ModelAttribute Item item, BindingResult bindingResult){
        //특정 필드가 아닌 복합룰 검증
        if(item.getPrice() != null && item.getQuantity()!= null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,resultPrice}, null);
            }
        }
        if(bindingResult.hasErrors()){
            log.info("errors= {}", bindingResult);
            return "form/v3/editForm";
        }

        itemRepository.update(itemId,item);
        return "redirect:/form/v3/items/{itemId}";
    }


    /**
     *  테스트용 데이터 추가
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
