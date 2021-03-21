package com.metapack.pizzarestaurant;

import com.google.gson.*;
import com.metapack.pizzarestaurant.entity.Item;
import com.metapack.pizzarestaurant.entity.Product;
import com.metapack.pizzarestaurant.entity.ProductParse;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sun.mail.smtp.SMTPSaslAuthenticator;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Controller
public class OrderController {

    private static final Logger log = Logger.getLogger(String.valueOf(OrderController.class));
    File file_cart = new File("src/main/resources/static/json/cart.json");
    int i=0;
    int totalPrice=0;
    List<Item> orderList = new ArrayList<>();

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
    public String sendOrder(ModelMap modelMap, Model model) throws JSONException, IOException {
        Date date = new Date(System.currentTimeMillis());
        Product product = new Product(date.toString(),orderList);
        String json = new Gson().toJson(product);
        JsonParser parser = new JsonParser();
        JsonObject rootObejct = parser.parse(json).getAsJsonObject();
        JsonElement jsonElement = rootObejct.get("Food");
        Item[] items = new Gson().fromJson(jsonElement,Item[].class);
        modelMap.addAttribute("priceCart",totalPrice);
        model.addAttribute("orderList",items);
        model.addAttribute("orderListCount",items.length);
        return "order_summary";
    }

    @CacheEvict(value = "totalPrice", allEntries = true)
    @GetMapping("/remove-element/{id}")
    public String removeElement(@PathVariable int id, ModelMap modelMap){
        totalPrice=totalPrice-(orderList.get(id).amount*orderList.get(id).foodPrice);
        orderList.remove(id);
        modelMap.addAttribute("priceCart",totalPrice);
        modelMap.addAttribute("orderList",orderList);
        return "redirect:/send-order";
    }

    @CacheEvict(value = "totalPrice",allEntries = true)
    @GetMapping("/history-order-list")
    public String getOrderHistoryList(ModelMap modelMap){
        String[] fileList;
        File path = new File("I:\\PROGRAMUJEMY\\JAVA\\Metapack\\pizza-restaurant\\src\\main\\resources\\static\\order_history");
        fileList = path.list();
        modelMap.addAttribute("fileList",fileList);
        modelMap.addAttribute("fileListCount",fileList.length);
        return "order_history_list";
    }

    @CacheEvict(value = "totalPrice",allEntries = true)
    @GetMapping("/open-file/{fileName}")
    public String openFile(@PathVariable String fileName,
            ModelMap modelMap) throws IOException {

        File file = new File( "I:\\PROGRAMUJEMY\\JAVA\\Metapack\\pizza-restaurant\\src\\main\\resources\\static\\order_history\\" + fileName);
        String content = FileUtils.readFileToString(file,"UTF-8");
        List<ProductParse> listProducts = new CsvToBeanBuilder(new FileReader(file))
                .withType(ProductParse.class)
                .build()
                .parse();
        modelMap.addAttribute("content",listProducts);
        modelMap.addAttribute("contentCount",listProducts.size());
        return "history_order";
    }

    @CacheEvict(value = "totalPrice", allEntries = true)
    @PostMapping("/finalize-order")
    public String finalizeOrder(@RequestParam String email,
                                @RequestParam String phone,
                                ModelMap modelMap,
                                RedirectAttributes redirectAttributes) throws IOException, ParseException {

        int k;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss");
        String date = sdf.format(new Date(System.currentTimeMillis()));
        String tempString = "";

        try {
            Properties props = new Properties();
            props.put("mail.smtp.port", 465);
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.debug", "true");
            String emailFromSend = "hotrestaurantpizza@gmail.com";
            String password = "AcB123456789AbC";

            Session session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailFromSend, password);
                }
            });

            try {
                MimeMessage msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(emailFromSend));
                msg.setRecipients(Message.RecipientType.TO, email);
                msg.setSubject("Zamówienie nr: " + date.toString());
                tempString = "Zamówienie nr: " + date.toString() + " \n";
                tempString = tempString + "Numer telefonu klienta: " + phone + "\n" +  "Lista zamówionych potraw: \n";
                for (k = 0; k <= orderList.size() - 1; k++) {
                    tempString = tempString + "\n" + k + ": " + orderList.get(k).foodName + " " + orderList.get(k).foodPrice
                            + "zł Ilość: " + orderList.get(k).amount + " " + " Suma: " + orderList.get(k).foodPrice * orderList.get(k).amount + "zł\n";
                }
                tempString = tempString + "\nKoszt całkowity to: " + totalPrice;
                tempString = tempString + "\n\n Pozdrawiamy, \nRestauracja Pizza Hot";
                msg.setText(tempString);

                Transport t = session.getTransport("smtp");
                t.connect(emailFromSend, password);
                t.sendMessage(msg, msg.getAllRecipients());
                t.close();

            } catch (AddressException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("msgError", "Zamówienie nie zostało zrealizowane. Potwierdzenie nie zostało wysłane");
                return "redirect:/menu";
            } catch (MessagingException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("msgError", "Zamówienie nie zostało zrealizowane. Potwierdzenie nie zostało wysłane");
                return "redirect:/menu";
            }
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("msgError", "Zamówienie nie zostało zrealizowane. Potwierdzenie nie zostało wysłane");
            return "redirect:/menu";
        }

        try {
            saveOrderToFolderHistory(date.toString(), email, phone);
        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("msgError", "Zamówienie nie zostało zrealizowane. Problem z zapisaniem zamówienia");
            return "redirect:/menu";
        }

        i=0;
        totalPrice=0;
        orderList.clear();
        modelMap.addAttribute("priceCart",totalPrice);
        redirectAttributes.addFlashAttribute("msgOrderSuccess", "Zamówienie zostało wysłane. Potwierdzenie znajdą Państwo na: " + email );
        return "redirect:/menu";
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
        orderList.add(new Item(fish.foodName(),fish.getCost(),quantity));
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
        orderList.add(new Item(hcake.foodName(),hcake.getCost(),quantity));
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
                orderList.add(new Item(coffee.foodName(),coffee.getCost(),quantity));
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
                orderList.add(new Item(tea.foodName(),tea.getCost(),quantity));
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
                orderList.add(new Item(cola.foodName(),cola.getCost(),quantity));
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
                orderList.add(new Item(tomatoe_soup.foodName(),tomatoe_soup.getCost(),quantity));
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
                orderList.add(new Item(chicken_soup.foodName(),chicken_soup.getCost(),quantity));
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

        //-----------------------------------------------------------------------------------
        //Normal I will to use this function from Decorator Pattern, but I wanted to show Builder Pattern
        //Schab will be Food class
        //orderToJSON(schab,quantity);
        //-----------------------------------------------------------------------------------

        totalPrice=totalPrice+(schab.getPrice()*quantity);
        orderList.add(new Item(schab.getName(),schab.getPrice(),quantity));
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
                orderList.add(new Item(details_margherita.foodName(),details_margherita.getCost(),quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";
            case "Vegetariana":
                Food vegetariana = new PizzaVegetarianaImpl();
                Food details_vegetariana = checkPizzaAddons(vegetariana,double_cheese,salami,ham,mushrooms);
                totalPrice=totalPrice+(details_vegetariana.getCost()*quantity);
                orderList.add(new Item(details_vegetariana.foodName(),details_vegetariana.getCost(),quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";
            case "Tosca":
                Food tosca = new PizzaToscaImpl();
                Food details_tosca = checkPizzaAddons(tosca,double_cheese,salami,ham,mushrooms);
                totalPrice=totalPrice+(details_tosca.getCost()*quantity);
                //arrayList = addJsonObjToArray(orderToJSON(details_tosca,quantity));
                orderList.add(new Item(details_tosca.foodName(),details_tosca.getCost(),quantity));
                i++;
                model.addAttribute("priceCart",totalPrice);
                return "redirect:/menu";
            case "Venecia":
                Food venecia = new PizzaVeneciaImpl();
                Food details_venecia = checkPizzaAddons(venecia,double_cheese,salami,ham,mushrooms);
                totalPrice=totalPrice+(details_venecia.getCost()*quantity);
                orderList.add(new Item(details_venecia.foodName(),details_venecia.getCost(),quantity));
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


    private void saveOrderToFolderHistory(String date, String email, String phone) throws IOException {
        Path path = Paths.get(("I:\\PROGRAMUJEMY\\JAVA\\Metapack\\pizza-restaurant\\src\\main\\resources\\static\\order_history\\"));
        File file = new File(path+"\\"+date+".csv");
        if(Files.exists(path)){
            FileUtils.writeStringToFile(file, "LP,Zamówienie,Cena jednostkowa,Ilość,Suma,Email,Telefon\n","UTF-8",true);
            for(int k=0;k<=orderList.size()-1;k++){
                FileUtils.writeStringToFile(file, "\"" + orderList.get(k).foodName +
                        "\",\"" + orderList.get(k).foodPrice + "\",\"" + orderList.get(k).amount +
                        "\",\"" + orderList.get(k).foodPrice * orderList.get(k).amount + "\",\"\",\"\"\n" , "UTF-8", true);
            }
            FileUtils.writeStringToFile(file, "\"\",\"\",\"\",\"\",\"\",\""+email+"\","+"\""+phone+"\"\n","UTF-8",true);
        }else {
           log.warning("Error with method saveOrderToFolderHistory");
        }
    }

}
