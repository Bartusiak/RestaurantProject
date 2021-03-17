package com.metapack.pizzarestaurant;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

@Controller
public class OrderController {

    private static final Logger log = Logger.getLogger(String.valueOf(OrderController.class));
    JSONObject jsonObject = new JSONObject();
    ArrayList<String> arrayList = new ArrayList<>();
    File file_cart = new File("src/main/resources/static/json/cart.json");
    int i=0;
    int totalPrice=0;

    public OrderController() throws IOException {
        log.warning("Create file");
        File file_cart = new File("/json/cart.json");
    }

    @CacheEvict(value = "totalPrice", allEntries = true)
    @GetMapping("/menu")
    public String menu(ModelMap modelMap){
        modelMap.addAttribute("priceCart",totalPrice);
        return "menu";
    }

    @CacheEvict(value = "totalPrice", allEntries = true)
    @GetMapping("/send-order")
    public String sendOrder(ModelMap modelMap){
        log.warning(arrayList.toString());
        modelMap.addAttribute("priceCart",totalPrice);
        modelMap.addAttribute("orderList",arrayList);
        return "order_summary";
    }

    @CacheEvict(value = "totalPrice", allEntries = true)
    @GetMapping("/order-pizza/{id}")
    public String orderPizza(@PathVariable Integer id,
                             ModelMap modelMap){
        modelMap.addAttribute("priceCart",totalPrice);
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
        return "menu";
    }

    @CacheEvict(value = "totalPrice", allEntries = true)
    @GetMapping("/order-schab")
    public String orderSchab(ModelMap modelMap){
        modelMap.addAttribute("priceCart",totalPrice);
        return "schab";
    }

    @CacheEvict(value = "totalPrice", allEntries = true)
    @GetMapping("/order-fish")
    public String orderFish(ModelMap modelMap){
        modelMap.addAttribute("priceCart",totalPrice);
        return "fish";
    }

    @Cacheable(value = "totalPrice",key = "totalPrice")
    @PostMapping("/addFishToOrderList")
    public String addFishToOrderList(Model model,
                                      @RequestParam("quantity")int quantity) throws JSONException {
        Food fish = new Food() {
            @Override
            public String foodName() {
                return "Ryba z frytkami";
            }

            @Override
            public int getCost() {
                return 28;
            }
        };
        totalPrice=totalPrice+(fish.getCost()*quantity);
        arrayList = addJsonObjToArray(orderToJSON(fish,quantity));
        i++;
        model.addAttribute("priceCart",totalPrice);
        return "redirect:/menu";
    }

    @CacheEvict(value = "totalPrice", allEntries = true)
    @GetMapping("/order-hungary-cake")
    public String orderHungaryCake(ModelMap modelMap){
        modelMap.addAttribute("priceCart",totalPrice);
        return "hungary_cake";
    }

    @Cacheable(value = "totalPrice",key = "totalPrice")
    @PostMapping("/addHungaryCakeToOrderList")
    public String addHungaryCakeToOrderList(Model model,
                                      @RequestParam("quantity")int quantity) throws JSONException {
        Food hcake = new Food() {
            @Override
            public String foodName() {
                return "Placek po węgiersku";
            }

            @Override
            public int getCost() {
                return 27;
            }
        };
        totalPrice=totalPrice+(hcake.getCost()*quantity);
        arrayList = addJsonObjToArray(orderToJSON(hcake,quantity));
        i++;
        model.addAttribute("priceCart",totalPrice);
        return "redirect:/menu";
    }

    @CacheEvict(value = "totalPrice", allEntries = true)
    @GetMapping("/order-soup/{id}")
    public String orderSoup(Model model, @PathVariable Integer id){
        switch (id){
            case 0:
                model.addAttribute("priceCart",totalPrice);
                return "tomatoe_soup";
            case 1:
                model.addAttribute("priceCart",totalPrice);
                return "chicken_soup";
        }
        model.addAttribute("priceCart",totalPrice);
        return "/menu";
    }

    @CacheEvict(value = "totalPrice", allEntries = true)
    @GetMapping("/order-drink/{id}")
    public String orderDrink(Model model, @PathVariable Integer id){
        switch (id){
            case 0:
                model.addAttribute("priceCart",totalPrice);
                return "coffee";
            case 1:
                model.addAttribute("priceCart",totalPrice);
                return "tea";
            case 2:
                model.addAttribute("priceCart",totalPrice);
                return "cola";
        }
        model.addAttribute("priceCart",totalPrice);
        return "/menu";
    }

    @Cacheable(value = "totalPrice",key = "totalPrice")
    @PostMapping("/addDrinkToOrderList/{drink}")
    public String addDrinkToOrderList(Model model,
                                     @PathVariable String drink,
                                     @RequestParam("quantity")int quantity) throws JSONException {
        switch (drink){
            case "coffee":
                Food coffee = new Food() {
                    @Override
                    public String foodName() {
                        return "Kawa";
                    }

                    @Override
                    public int getCost() {
                        return 5;
                    }
                };
                totalPrice=totalPrice+(coffee.getCost()*quantity);
                arrayList = addJsonObjToArray(orderToJSON(coffee,quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";

            case "tea":
                Food tea = new Food() {
                    @Override
                    public String foodName() {
                        return "Herbata";
                    }

                    @Override
                    public int getCost() {
                        return 5;
                    }
                };
                totalPrice=totalPrice+(tea.getCost()*quantity);
                arrayList = addJsonObjToArray(orderToJSON(tea,quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";

            case "cola":
                Food cola = new Food() {
                    @Override
                    public String foodName() {
                        return "Cola";
                    }

                    @Override
                    public int getCost() {
                        return 5;
                    }
                };
                totalPrice=totalPrice+(cola.getCost()*quantity);
                arrayList = addJsonObjToArray(orderToJSON(cola,quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";
        }
        model.addAttribute("priceCart",totalPrice);
        return "redirect:/menu";
    }


    @Cacheable(value = "totalPrice",key = "totalPrice")
    @PostMapping("/addSoupToOrderList/{soup}")
    public String addSoupToOrderList(Model model,
                                            @PathVariable String soup,
                                            @RequestParam("quantity")int quantity) throws JSONException {
        switch (soup){
            case "tomatoe_soup":
                Food tomatoe_soup = new Food() {
                    @Override
                    public String foodName() {
                        return "Zupa pomidorowa";
                    }

                    @Override
                    public int getCost() {
                        return 12;
                    }
                };
                totalPrice=totalPrice+(tomatoe_soup.getCost()*quantity);
                arrayList = addJsonObjToArray(orderToJSON(tomatoe_soup,quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";

            case "chicken_soup":
                Food chicken_soup = new Food() {
                    @Override
                    public String foodName() {
                        return "Rosół";
                    }

                    @Override
                    public int getCost() {
                        return 10;
                    }
                };
                totalPrice=totalPrice+(chicken_soup.getCost()*quantity);
                arrayList = addJsonObjToArray(orderToJSON(chicken_soup,quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";
        }
        model.addAttribute("priceCart",totalPrice);
        return "redirect:/menu";
    }


    @Cacheable(value = "totalPrice",key = "totalPrice")
    @PostMapping("/addSchabToOrderList")
    public String addSchabToOrderList(Model model, ModelMap modelMap,
                                      @RequestParam(value = "addon") String addon,
                                      @RequestParam("quantity")int quantity) throws JSONException {
        Schab schab = Schab.builder().name("Schab z " + addon)
                                    .price(30).build();
        //Normal I will to use this function from Decorator Pattern, but I wanted to show Builder Pattern
        //Schab will be Food class
        //orderToJSON(schab,quantity);
        //-----------------------------------------------------------------------------------
        totalPrice=totalPrice+(schab.getPrice()*quantity);
        arrayList = addJsonObjToArray(orderSchabToJSON(schab,quantity));
        i++;
        model.addAttribute("priceCart",totalPrice);
        return "redirect:/menu";
    }

    @Cacheable(value = "totalPrice",key = "totalPrice")
    @PostMapping("/addPizzaToOrderList/{pizza}")
    public String addPizzaToCart(Model model, ModelMap modelMap,
                                 @PathVariable("pizza")String pizzaName,
                                 @RequestParam(value = "double_cheese", required = false)Boolean double_cheese,
                                 @RequestParam(value = "salami", required = false)Boolean salami,
                                 @RequestParam(value = "ham", required = false)Boolean ham,
                                 @RequestParam(value = "mushrooms", required = false)Boolean mushrooms,
                                 @RequestParam("quantity")int quantity) throws JSONException, IOException {

        switch (pizzaName){
            case "Margherita":
                Food margherita = new PizzaMargheritaImpl();
                Food details_margherita = checkPizzaAddons(margherita,double_cheese,salami,ham,mushrooms);
                totalPrice=totalPrice+(details_margherita.getCost()*quantity);
                arrayList = addJsonObjToArray(orderToJSON(details_margherita,quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";
            case "Vegetariana":
                Food vegetariana = new PizzaVegetarianaImpl();
                Food details_vegetariana = checkPizzaAddons(vegetariana,double_cheese,salami,ham,mushrooms);
                totalPrice=totalPrice+(details_vegetariana.getCost()*quantity);
                arrayList = addJsonObjToArray(orderToJSON(details_vegetariana,quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";
            case "Tosca":
                Food tosca = new PizzaToscaImpl();
                Food details_tosca = checkPizzaAddons(tosca,double_cheese,salami,ham,mushrooms);
                totalPrice=totalPrice+(details_tosca.getCost()*quantity);
                arrayList = addJsonObjToArray(orderToJSON(details_tosca,quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";
            case "Venecia":
                Food venecia = new PizzaVeneciaImpl();
                Food details_venecia = checkPizzaAddons(venecia,double_cheese,salami,ham,mushrooms);
                totalPrice=totalPrice+(details_venecia.getCost()*quantity);
                arrayList = addJsonObjToArray(orderToJSON(details_venecia,quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";
        }
        model.addAttribute("priceCart",totalPrice);
        return "redirect:/menu";
    }





    public Food checkPizzaAddons(Food pizza, Boolean double_cheese, Boolean salami, Boolean ham, Boolean mushrooms){
        if(double_cheese!=null){
            pizza = new DoubleCheese(pizza);
        }
        if(salami!=null){
            pizza = new Salami(pizza);
        }
        if(ham!=null){
            pizza = new Ham(pizza);
        }
        if(mushrooms!=null){
            pizza = new Mushrooms(pizza);
        }
        return pizza;
    }

    public JSONObject orderToJSON(Food food, int quantity) throws JSONException {
        JSONObject foodObj = new JSONObject(jsonObject);
        foodObj.put("foodName", food.foodName());
        foodObj.put("foodPrice", food.getCost()*quantity);
        foodObj.put("amount", quantity);
        jsonObject.put("Id",i);
        jsonObject.put("Food",foodObj);
        return jsonObject;
    }

    public JSONObject orderSchabToJSON(Schab schabObj, int quantity) throws JSONException {
        JSONObject foodObj = new JSONObject(jsonObject);
        foodObj.put("foodName", schabObj.getName());
        foodObj.put("foodPrice", (schabObj.getPrice()*quantity));
        foodObj.put("amount", quantity);
        jsonObject.put("Id",i);
        jsonObject.put("Food",foodObj);
        return jsonObject;
    }

    public void saveJsonObjToFile(JSONObject obj) throws IOException {
        FileUtils.touch(file_cart);
        try {
            FileUtils.writeStringToFile(file_cart, obj.toString(), "UTF-8");
        }catch (IOException e){
            log.warning("AddPizzaToOrder error while writing file: " +  e);
        }
    }

    public ArrayList<String> addJsonObjToArray(JSONObject obj){
        arrayList.add(i,obj.toString());
        return arrayList;
    }
}
