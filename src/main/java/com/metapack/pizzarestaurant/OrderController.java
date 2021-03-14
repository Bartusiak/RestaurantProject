package com.metapack.pizzarestaurant;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.tags.Param;

import java.util.logging.Logger;

@Controller
public class OrderController {

    private static final Logger log = Logger.getLogger(String.valueOf(OrderController.class));

    @GetMapping("/menu")
    public String menu(){
        return "pizza_order";
    }

    @GetMapping("/order-pizza/{id}")
    public String orderPizza(@PathVariable Integer id){
        switch (id){
            case 1:
                return "pizza_margherita";
            case 2:
                return "pizza_vegetariana";
            case 3:
                return "pizza_tosca";
            case 4:
                return "pizza_venecia";
        }
        return "pizza_order";
    }

    @PostMapping("/addToOrderList")
    public String addToCart(Model model, ModelMap modelMap,
                            @RequestParam("quantity")int quantity){

        log.warning("Add to cart");
        log.warning("Amount: " + quantity);
        log.warning( "DoubleCheese: ");
        return "redirect:/menu";
    }



}
