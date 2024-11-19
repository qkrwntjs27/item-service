package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String save() {
        return "basic/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes ) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    @PostMapping("{itemId}/delete")
    public String deleteItem(@PathVariable("itemId") Long itemId) {
        itemRepository.delete(itemId);
        return "redirect:/basic/items";
    }

    @GetMapping("/search")
    public String searchItem(@RequestParam("query") String query, Model model) {
        // ID로 검색
        try {
            Long id = Long.valueOf(query);
            Item item = itemRepository.findById(id);
            model.addAttribute("item", item);
            return "basic/select";
        } catch (NumberFormatException e) {
            // 상품명으로 검색
            List<Item> items = itemRepository.findAll();
            Item item = items.stream()
                    .filter(i -> i.getItemName().equalsIgnoreCase(query))
                    .findFirst()
                    .orElse(null);
            model.addAttribute("item", item);
            return "basic/select";
        }
    }


    //testcase
    @PostConstruct
    public void init() {

        ItemRepository.save(new Item("testA", 10000, 10));
        ItemRepository.save(new Item("testB", 20000, 10));

    }
}
