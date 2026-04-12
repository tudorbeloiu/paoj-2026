package com.pao.proiect.licitatii;

import com.pao.proiect.licitatii.model.enums.AuctionType;
import com.pao.proiect.licitatii.model.entities.Buyer;
import com.pao.proiect.licitatii.model.entities.Seller;
import com.pao.proiect.licitatii.model.entities.User;
import com.pao.proiect.licitatii.exception.*;
import com.pao.proiect.licitatii.model.products.*;
import com.pao.proiect.licitatii.model.transactions.Notification;
import com.pao.proiect.licitatii.service.*;

import java.time.LocalDateTime;
import java.util.*;

public class Main{

    private static UserService userService = UserService.getInstance();
    private static AuctionService auctionService = AuctionService.getInstance();
    private static ProductService productService = ProductService.getInstance();
    private static CategoryService categoryService = CategoryService.getInstance();
    private static BidService bidService = BidService.getInstance();
    private static TransactionService transactionService = TransactionService.getInstance();
    private static NotificationService notificationService = NotificationService.getInstance();


    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);


        while(true){
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1:
                    printSubmenuUser(scanner);
                    break;
                case 2:
                    printSubmenuProductCateg(scanner);
                    break;
                case 3:
                    printSubmenuAuction(scanner);
                    break;
                case 4:
                    printSubmenuBid(scanner);
                    break;
                case 5:
                    printSubmenuTransactions(scanner);
                    break;
                case 6:
                    printSubmenuNotifications(scanner);
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }
    private static void printMenu(){
        System.out.println("MAIN MENU\n" +
                "1. User Management\n" +
                "2. Product & Category Management\n" +
                "3. Auction Management  \n" +
                "4. Bid Management\n" +
                "5. Transactions & Reports\n" +
                "6. Notifications\n" +
                "0. Exit\n"+
                "Your choice: ");
    }
    private static void printSubmenuUser(Scanner scanner){
        while(true){
            System.out.println("1. Register Buyer\n" +
                    "2. Register Seller\n" +
                    "3. Find user by email\n" +
                    "4. Delete user by ID\n" +
                    "5. Show all users\n" +
                    "0. Back\n"+
                    "Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1:
                    System.out.print("Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Email: ");
                    String email = scanner.next();

                    System.out.print("Password: ");
                    String password = scanner.next();

                    System.out.print("Balance: ");
                    double balance =scanner.nextDouble();
                    scanner.nextLine();

                    try{
                        User buyer = new Buyer(name, email, password, balance);
                        userService.addUser(buyer);
                        System.out.println("Buyer added: " + buyer);
                    }
                    catch(EmailAlreadyExists | InvalidNumber e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Name: ");
                    String nameSeller = scanner.nextLine();

                    System.out.print("Email: ");
                    String emailSeller = scanner.next();

                    System.out.print("Password: ");
                    String passwordSeller = scanner.next();

                    try{
                        User seller = new Seller(nameSeller, emailSeller, passwordSeller);
                        userService.addUser(seller);
                        System.out.println("Seller added: " + seller);
                    }
                    catch(EmailAlreadyExists e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("User's email: ");
                    String userByEmail = scanner.next();
                    try{
                        User user = userService.findUserByEmail(userByEmail);
                        System.out.println(user);
                    }
                    catch (UserNotFound e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Enter the id of the user you want to delete: ");
                    String toDeleteId = scanner.next();

                    try{
                        userService.deleteUserById(toDeleteId);
                        System.out.println("User deleted successfully!");
                    }
                    catch(UserNotFound e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    userService.printAllUsers();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }

    }
    private static void printSubmenuAuction(Scanner scanner){

        while(true){
            System.out.println("1. Create auction\n" +
                    "2. Show active auctions\n" +
                    "3. Show auctions by category\n" +
                    "4. Show auctions by seller\n" +
                    "5. Show auctions in price range\n" +
                    "6. Close auction\n" +
                    "7. Cancel auction\n" +
                    "0. Back\n"+
                    "Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                //Seller seller, Product product, double startingPrice, LocalDateTime startingTime, LocalDateTime endingTime, AuctionType auctionType
                case 1:
                    try{
                        System.out.print("ID of the seller: ");
                        String sellerId = scanner.next();
                        User seller1 = userService.findUserById(sellerId);

                        if(!(seller1 instanceof Seller seller)){
                            System.out.println("[ERROR] User is not a seller!");
                            break;
                        }

                        System.out.print("Product ID: ");
                        String productId = scanner.next();
                        Product product = productService.findProductById(productId);


                        System.out.print("Starting price: ");
                        double startingPrice = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.println("Auction type -> (1=ENGLISH, 2=DUTCH, 3=SEALED_BID): ");
                        int typeChoice = scanner.nextInt();
                        scanner.nextLine();

                        AuctionType auctionType = AuctionType.ENGLISH;
                        switch (typeChoice){
                            case 2:
                                auctionType =AuctionType.DUTCH;
                                break;
                            case 3:
                                auctionType = AuctionType.SEALED_BID;
                                break;
                            default:
                                auctionType = AuctionType.ENGLISH;
                                break;
                        }
                        System.out.print("Duration in minutes: ");
                        int minutes = scanner.nextInt();
                        scanner.nextLine();

                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime endTime = now.plusMinutes(minutes);



                        auctionService.createAuction(seller, product, startingPrice, now, endTime, auctionType);
                        System.out.println("Auction created successfully!");

                    }
                    catch(UserNotFound | ProductNotExist | ProductAlreadyAtAuction | InvalidNumber | InvalidAuctionDate e){
                        System.out.println(e.getMessage());
                    }
                    break;


                case 2:
                    auctionService.showActiveAuctions();
                    break;

                case 3:
                    System.out.print("Category id: ");
                    String categoryId = scanner.next();
                    try{
                        Category category = categoryService.findCategoryById(categoryId);
                        List<Auction> auctions =   auctionService.getAuctionsByCategory(category);
                        for(Auction auct: auctions){
                            System.out.println(auct);
                        }
                    }
                    catch(CategoryNotExist e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Seller id: ");
                    String sellerId = scanner.next();
                    try{
                        User seller1 = userService.findUserById(sellerId);

                        if(!(seller1 instanceof Seller seller)){
                            System.out.println("[ERROR] User is not a seller!");
                            break;
                        }

                        List<Auction> auctions = auctionService.getAuctionsBySeller(seller);
                        for(Auction auct: auctions){
                            System.out.println(auct);
                        }
                    }
                    catch(UserNotFound e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    try{
                        System.out.print("Left bound value: ");
                        double leftBound = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.print("Right bound value: ");
                        double rightBound = scanner.nextDouble();
                        scanner.nextLine();

                        auctionService.showAuctionsInInterval(leftBound, rightBound);
                    }
                    catch(InvalidNumber e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 6:
                    try{
                        System.out.print("Enter the ID of the auction you want to close: ");
                        String auctionId = scanner.next();

                        Auction auction = auctionService.getAuctionById(auctionId);

                        auctionService.closeAuction(auction);
                    }
                    catch(AuctionNotExists | AuctionNotActive | AuctionStillActive | NoBiddingsError e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 7:
                    try{
                        System.out.print("Enter the ID of the auction you want to cancel: ");
                        String auctionId = scanner.next();

                        Auction auction = auctionService.getAuctionById(auctionId);

                        auctionService.cancelAuction(auction);
                    }
                    catch(AuctionNotExists | AuctionNotActive e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid option!");
                    break;

            }
        }

    }
    private static void printSubmenuBid(Scanner scanner){

        while(true){
            System.out.println("1. Place bid\n" +
                    "2. Show bids for auction\n" +
                    "0. Back\n"+
                    "Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                //placeBid(Buyer bidder, Auction auction, double amount)
                case 1:
                    try{
                        System.out.print("Buyer id: ");
                        String buyerId = scanner.next();

                        User buyer1 = userService.findUserById(buyerId);

                        if(!(buyer1 instanceof Buyer buyer)){
                            System.out.println("User is not a buyer!");
                            break;
                        }

                        System.out.print("Auction id: ");
                        String auctionId = scanner.next();

                        Auction auction = auctionService.getAuctionById(auctionId);

                        System.out.print("Bidding amount: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();

                        auctionService.placeBid(buyer, auction, amount);
                    }
                    catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try{
                        System.out.print("Auction id: ");
                        String auctionId = scanner.next();

                        Auction auction = auctionService.getAuctionById(auctionId);

                        auctionService.showBidsForAuction(auction);
                    }
                    catch(AuctionNotExists e){
                        System.out.print(e.getMessage());
                    }
                    break;

                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;

            }
        }

    }
    private static void printSubmenuTransactions(Scanner scanner){
        while(true){
            System.out.println("1. Show all transactions\n" +
                    "2. Show transactions for user\n" +
                    "0. Back\n"+
                    "Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    transactionService.printAllTransactions();
                    break;
                case 2:
                    try{
                        System.out.print("User ID: ");
                        String userId = scanner.next();

                        User user = userService.findUserById(userId);
                        transactionService.printTransactionsOfUser(user);
                    }
                    catch(UserNotFound e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }

    }
    private static void printSubmenuNotifications(Scanner scanner){
        while(true){
            System.out.println("1. Show unread notifications\n" +
                    "2. Mark notification as read\n" +
                    "0. Back\n" +
                    "Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    try{
                        System.out.print("Enter the ID of the user you want to see its unread notifications: ");
                        String userId = scanner.next();

                        User user = userService.findUserById(userId);
                        TreeSet<Notification> unreadNotifs = notificationService.unreadNotifications(user);
                        for(Notification notif: unreadNotifs){
                            System.out.println(notif);
                        }
                    }
                    catch(UserNotFound e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try{
                        System.out.print("Enter the User ID: ");
                        String userId = scanner.next();

                        User user = userService.findUserById(userId);

                        System.out.print("Unread notification ID: ");
                        String notifId = scanner.next();

                        notificationService.markRead(user, notifId);
                    }
                    catch(UserNotFound e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;

            }
        }

    }
    private static void printSubmenuProductCateg(Scanner scanner){
        while(true){
            System.out.println("1. Add category\n" +
                    "2. Show all categories\n" +
                    "3. Add physical product\n" +
                    "4. Add digital product\n" +
                    "5. Show all products\n" +
                    "6. Find product by ID\n" +
                    "0. Back\n"+
                    "Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    System.out.print("Name of the category: ");
                    String name = scanner.nextLine();

                    System.out.print("Description of the category: ");
                    String description = scanner.nextLine();

                    categoryService.addCategory(new Category(name, description));
                    break;

                case 2:
                    categoryService.printAllCategories();
                    break;
                case 3:
                    try{
                        System.out.print("Name of the product: ");
                        String namePhyProduct = scanner.nextLine();

                        System.out.print("Description of the product: ");
                        String descriptionPhyProduct = scanner.nextLine();

                        System.out.print("Price: ");
                        double price = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.print("Category id: ");
                        String categId = scanner.next();

                        Category categ = categoryService.findCategoryById(categId);

                        System.out.print("Manufacture year: ");
                        int year = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Weight: ");
                        double weight = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.print("Seller id: ");
                        String sellerId = scanner.next();

                        User seller1 = userService.findUserById(sellerId);
                        if(!(seller1 instanceof Seller seller)){
                            System.out.println("User is not a seller!");
                            break;
                        }

                        productService.addProduct(new PhysicalProduct(namePhyProduct,
                                descriptionPhyProduct, price, categ, year, weight), seller);

                        System.out.println("Physical product added!");
                    }
                    catch(UserNotFound | CategoryNotExist | InvalidYear | InvalidNumber e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    try{
                        System.out.print("Name of the product: ");
                        String nameProductDigital = scanner.nextLine();

                        System.out.print("Description of the product: ");
                        String descriptionProductDigital = scanner.nextLine();

                        System.out.print("Price: ");
                        double priceDigital = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.print("Category id: ");
                        String categIdDigital = scanner.next();

                        Category categDigital = categoryService.findCategoryById(categIdDigital);

                        System.out.print("Format: ");
                        String format = scanner.next();

                        System.out.print("License key: ");
                        String licenseKey = scanner.next();

                        System.out.print("Seller id: ");
                        String sellerId = scanner.next();

                        User seller1 = userService.findUserById(sellerId);
                        if(!(seller1 instanceof Seller seller)){
                            System.out.println("User is not a seller!");
                            break;
                        }

                        productService.addProduct(new DigitalProduct(nameProductDigital, descriptionProductDigital,
                                priceDigital, categDigital, format, licenseKey), seller);

                        System.out.println("Digital product added!");

                    }
                    catch(UserNotFound | CategoryNotExist | InvalidDigitalFormat e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    productService.printAllProducts();
                    break;
                case 6:
                    try{
                        System.out.print("Enter product ID: ");
                        String productId = scanner.next();

                        Product product = productService.findProductById(productId);
                        System.out.println(product);
                    }
                    catch(ProductNotExist e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 0:
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }

    }
}