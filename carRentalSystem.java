import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car {

    private String carID;
    private String carBrand;
    private String carModel;

    private double carRent;
    private boolean isAvailable;

    public Car(String carId, String carBrand, String carModel, double carRent) {
        this.carID = carId;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carRent = carRent;
        this.isAvailable = true;
    }

    // Getters :-
    public String getCarId() {
        return carID;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public double getCarRent() {
        return carRent;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // ___________________________

    // car went in rent
    public void rent() {
        isAvailable = false;
    }

    // car comes from rent
    public void returnedFromRent() {
        isAvailable = true;
    }

    // ___________________________

    // Total rent of car to take from customer
    public double calculateTotalRent(int rentalDays) {
        return carRent * rentalDays;
    }
}

class Customer {
    private String name;
    private String customerId;
    private long mobileNumber;

    public Customer(String customerId, String name, long mobileNumber) {
        this.customerId = customerId;
        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    // Getters :

    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int rentalDays;

    public Rental(Car car, Customer customer, int rentalDays) {
        this.car = car;
        this.customer = customer;
        this.rentalDays = rentalDays;
    }

    // Getters :
    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int rentalDays() {
        return rentalDays;
    }
}

public class carRentalSystem {

    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public carRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    // Renting a car by Customer
    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
        } else {
            System.out.println("Car is not available for rent!");
        }
    }

    // Returning a car by customer
    public void returnCar(Car car) {

        Rental rentalToRemove = null;

        // verify that the customer was really took the car on rent or not
        for (Rental r : rentals) {
            if (r.getCar() == car) {
                rentalToRemove = r;
                break;
            }
        }

        if (rentalToRemove != null) {
            rentalToRemove.getCar().returnedFromRent(); // mark available car
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Car was not on rent to you from Garage.");
        }
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println(" \n ===== Car Rental System ===== \n");

            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");

            System.out.print("\nEnter your choice from (1, 2, or 3) : ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.println("\n -:=== Rent a Car ===:- \n");

                System.out.print("Enter your name : ");
                String customerName = sc.nextLine();

                System.out.print("Enter your Mobile Number : ");
                long mobileN = sc.nextLong();
                sc.nextLine();

                System.out.println("\nAvailable Cars :- ");

                for (Car c : cars) {
                    if (c.isAvailable()) {
                        System.out.println(c.getCarId() + " : " + c.getCarBrand() + " " + c.getCarModel());
                    }
                }

                System.out.print("\nEnter Car ID you want on Rent : ");
                String carId = sc.nextLine();

                System.out.print("Enter the number of days for Rental : ");
                int rentalDays = sc.nextInt();
                sc.nextLine();

                // Adding new customer details in data
                Customer newCustomer = new Customer("CUS" + mobileN, customerName, mobileN);
                addCustomer(newCustomer);

                Car selectedCar = null;

                for (Car c : cars) {
                    if (c.getCarId().equals(carId) && c.isAvailable()) {
                        selectedCar = c;
                        break;
                    }
                }

                if (selectedCar != null) {
                    double totalPrice = selectedCar.calculateTotalRent(rentalDays);

                    System.out.println("\n-:=== Rental Information ===:-\n");
                    System.out.println("Customer Name : " + newCustomer.getName());
                    System.out.println("Car : " + selectedCar.getCarBrand() + " " + selectedCar.getCarModel());
                    System.out.println("Rental Days : " + rentalDays);
                    System.out.println("Total Price : " + totalPrice + " INR");

                    System.out.print("\nConfirm Rental? (Yes / No) : ");
                    String confirm = sc.nextLine();

                    if (confirm.equalsIgnoreCase("Yes")) {
                        rentCar(selectedCar, newCustomer, rentalDays);
                        System.out.println("\n---- Car rented Successfully! ----");
                    } else {
                        System.out.println("\nRent cancelled!");
                    }
                } else {
                    System.out.println("Car is not available for rent.");
                }
            }

            else if (choice == 2) {
                System.out.println("\n== Return a Car ==\n");

                System.out.print("Enter Car ID to return a Car : ");
                String returnCarId = sc.nextLine();

                Car carToReturn = null;
                for (Car c : cars) {
                    if (c.getCarId().equals(returnCarId) && !c.isAvailable()) {
                        carToReturn = c;
                        break;
                    }
                }

                if (carToReturn != null) {
                    Customer customer = null;

                    for (Rental r : rentals) {
                        if (r.getCar() == carToReturn) {
                            customer = r.getCustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnCar(carToReturn);
                        System.out.println("\nCar returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Car was not rented or Rental information is missing!");
                    }
                } else {
                    System.out.println("Car was not rented or Invalid Car ID!");
                }
            }

            else if (choice == 3) {
                break;
            }

            else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        System.out.println("Thank you for using The Car Rental System.");
    }

    public static void main(String[] args) {

        Car car1 = new Car("C001", "Honda", "Accord", 2000);
        Car car2 = new Car("C002", "Toyota", "Camry", 2500);
        Car car4 = new Car("C004", "BMW", "M5", 4000);
        Car car5 = new Car("C005", "Mercedes", "G-Wagon", 4000);
        Car car3 = new Car("C003", "Suzuki", "WagonR", 9000);

        carRentalSystem CRS = new carRentalSystem();

        CRS.addCar(car1);
        CRS.addCar(car2);
        CRS.addCar(car3);
        CRS.addCar(car4);
        CRS.addCar(car5);

        CRS.menu();
    }
}
