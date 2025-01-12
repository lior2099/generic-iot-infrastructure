package il.co.ilrd.admin_db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDB {


    @Test
    public void CreateTest(){
        AdminDB adminDB;

        try {
            adminDB = new AdminDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        AdminDB.Company company = new AdminDB.Company("lior" , "isreal" , "here" ,"not here" , "dudu" , "maya" , "052" );
        AdminDB.Company company2 = new AdminDB.Company("lior2" , "isreal" , "Why" ,"not here" , "dudu" , "ido" , "052" );
        AdminDB.Company company3 = new AdminDB.Company("lior5" , "isreal" , "Why" ,"not here" , "dudu" , "ido" , "052" );
//        adminDB.RegisterCompany(company);
//        adminDB.RegisterCompany(company2);
//        adminDB.RegisterCompany(company3);


        AdminDB.Product product = new AdminDB.Product("lior" , "1025" , "V1");
        AdminDB.Product product2 = new AdminDB.Product("lior5" , "X144" , "V1");
        AdminDB.Product product3 = new AdminDB.Product("lior5" , "X155" , "V1");
//        adminDB.RegisterProduct(product);
//        adminDB.RegisterProduct(product2);
//        adminDB.RegisterProduct(product3);

        AdminDB.Company companyCopy2 = adminDB.getCompanyInfo("lior2");

        assertEquals(companyCopy2.companyName , "lior2");
        assertEquals(companyCopy2.contactPhone , "052");
        assertEquals(companyCopy2.city , "Why");

        Collection<AdminDB.Product> products =  adminDB.getProductsInfo("lior5");

        for (AdminDB.Product iterProduct : products){
            System.out.println("companyName : " + iterProduct.companyName);
            System.out.println("model : " + iterProduct.model);
            System.out.println("version : " + iterProduct.version);
        }

        adminDB.upDateCompanyInfo("lior2" , "city", "+pata-tikva");
        AdminDB.Company companyafterupdata = adminDB.getCompanyInfo("lior2");

        System.out.println("companyName : " + companyafterupdata.companyName);
        System.out.println("city : " + companyafterupdata.city);

    }

}
