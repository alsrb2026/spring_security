package jpabook.jpashop.controller;


import jpabook.jpashop.domain.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/itemList")
    public String showItemList(Model model) {
        List<Item> items = itemService.findItems();

        model.addAttribute("items", items);
        return "/items/showItemList";
    }

    @GetMapping("/item/{id}")
    public String auctionItem(@PathVariable("id") Long id, Model model) {
        Item form = itemService.findOne(id);
        model.addAttribute("form", form);
        return "/board/auctionItemForm.html";
    }

    @GetMapping("items/{id}/auction")
    public String auctionItemForm(@PathVariable("id") Long itemId, Model model) {
        Item item = itemService.findOne(itemId);

        ItemForm form = new ItemForm();
        form.setId(item.getId());
        form.setItemUserId(item.getItemUserId());
        form.setName(item.getName());
        form.setTitle(item.getTitle());
        form.setStartBid(item.getStartBid());
        form.setWinningBid(item.getWinningBid());
        form.setUnitBid(item.getUnitBid());
        form.setDescription(item.getDescription());
        form.setStatus(item.getStatus());
        form.setImages(item.getImages());
        form.setCurrentBidId(item.getCurrentBidId());
        form.setCurrentBid(item.getCurrentBid());
        form.setStartAuctionTime(item.getStartAuctionTime());
        form.setStartAuctionTime(item.getStartAuctionTime());
        form.setAuctionPeriod(item.getAuctionPeriod());

        model.addAttribute("form", form);
        return "/board/auctionItemForm.html";
    }

    @PostMapping("/items/{id}/auction")
    public String auctionItem(@ModelAttribute("form")  ItemForm form){

        Item item = new Item();
        item.setId(form.getId());
        item.setItemUserId(form.getItemUserId());
        item.setName(form.getName());
        item.setTitle(form.getTitle());
        item.setStartBid(form.getStartBid());
        item.setWinningBid(form.getWinningBid());
        item.setUnitBid(form.getUnitBid());
        item.setDescription(form.getDescription());
        item.setStatus(form.getStatus());
        item.setImages(form.getImages());
        item.setCurrentBidId(form.getCurrentBidId());
        item.setCurrentBid(form.getCurrentBid());
        item.setStartAuctionTime(form.getStartAuctionTime());
        item.setAuctionPeriod(form.getAuctionPeriod());

        // ?????? ????????? ????????? ???????????? ?????????????????? ?????? ????????? ?????????????
        /*
        if(item.getCurrentBid() < item.getStartBid() || item.getCurrentBid() < item.getWinningBid()){
            return "";
        }
        */
        // ?????? ????????? ???????????? ?????? ??????, ?????? ???????????? ????????? ??????????????? ???????????????. ?????? id??? ??????, ????????? ????????? ?????? ?????????
        if(item.getCurrentBid() == item.getWinningBid()){
            item.setStatus("?????? ??????");
        }

        if(item.getCurrentBid() < item.getWinningBid()){
            item.setCurrentBid(item.getCurrentBid() + item.getUnitBid());
        }

        // ?????? ?????? ?????? ????????? ??? ????????? ??????

        itemService.saveItem(item); // service??? transaction=false??? ??????, repository??? saveItem??? em.flush()??? ??????
        // db??? ????????? ????????????.

        return "redirect:/";
    }
}
