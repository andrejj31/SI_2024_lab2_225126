package mk.ukim.finki;

import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SILab2Test {

    @Test
    void testEveryBranch() {
        // Prv test: (null, X), treba da vrati RuntimeException poradi null kako argument za lista
        RuntimeException ex;
        ex = assertThrows(RuntimeException.class, () -> SILab2.checkCart(null, 200));
        assertTrue(ex.getMessage().contains("allItems list can't be null!"));

        // Vtor test: (new Item(null, "12b456789", 50, 0)), X). Frla exception dokolku vo barkodot ima nedozvolena vrednost vo slucajov "b"
        ex = assertThrows(RuntimeException.class, () -> SILab2.checkCart(List.of(new Item(null, "12b456789", 50, 0)), 200));
        assertTrue(ex.getMessage().contains("Invalid character in item barcode!"));

        // Tret test: (new Item("Milka", null, 50, 0)), X). Frla exception bidejki barcodot e null
        ex = assertThrows(RuntimeException.class, () -> SILab2.checkCart(List.of(new Item("Milka", null, 50, 0)), 200));
        assertTrue(ex.getMessage().contains("No barcode!"));

        // Cetvrt test: (new Item("Milka", "133235", 100, 0.7),new Item("Milka", "133235", 50, 0)), 100). Potrebno e da vrati
        // true bidejki vkupnata cena dobiena od site produkti e 120, a plakjanjeto e 200 => 120<200 so sto vrakja true
        assertTrue(SILab2.checkCart(List.of(new Item("Milka", "245467", 100, (float)0.7), new Item("Milka", "133235", 50, 0)), 200));

        // Petti test: (new Item("Item", "033235", 500, (float)0.5), new Item("Milka", "133235", 50, 0)), 200). Potrebno e da vrati
        // false bidejki vkupnata cena na dobienite produkti e pomala od toa sto e plateno
        // Bidejki na prviot produkt ima popust 500*0.5 se dobiva 250. Prviot produkt vleguva vo
        // if (item.getPrice() > 300 && item.getDiscount() > 0 && item.getBarcode().charAt(0) == '0') a so toa sumata se
        // namaluva za 30 odnosno 250-30+50 = 270>200 so sto se vrakja false
        assertFalse(SILab2.checkCart(List.of(new Item("Item", "033235", 500, (float)0.5), new Item("Milka", "133235", 50, 0)), 200));
    }

    @Test
    void testMultipleCondition (){
        //(item.getPrice() > 300 && item.getDiscount() > 0 && item.getBarcode().charAt(0) == '0')

        // 1 Test case: TRUE (price>300) && TRUE (discount>0) && TRUE (barcode pocnuva so 0)
        // Bidejki ima popust novata cena kje bide 315, so ovoj test case bi trebalo da se vleze
        // vo uslovot so sto cenata bi se namalila za 30 i novata cena bi bila 285, i funkcijata bi trebalo
        // da vrati true
        assertTrue(SILab2.checkCart(List.of(new Item("Item1", "0123456789", 350, 0.9f)), 300));

        // 2 Test case: FALSE (price<300) && TRUE (discount>0) && TRUE (barcode pocnuva so 0)
        // Nova cena 290*0.9 = 261, bidejki ne e ispolnet prviot uslov ( price<300 ) nema da se namali dopolnitelno sumata za 30
        // Od tuka sleduva deka 261>250 i funkcijata bi trebalo da vrati false
        assertFalse(SILab2.checkCart(List.of(new Item("Item1", "0123456789", 300, 0.9f)), 250));

        // 3 Test case: TRUE (price>300) && FALSE (discount<=0) && TRUE (barcode pocnuva so 0)
        // Bidejki ne e ispolnet uslovot za discount cenata si ostanuva ista. Isto taka cenata ne se namaluva za 30 bidejki site
        // uslovi ne se ispolneti
        assertFalse(SILab2.checkCart(List.of(new Item("Item1", "0123456789", 400, 0)), 380));

        // 4 Test case: TRUE (price>300) && TRUE (discount>0) && FALSE (barcode ne pocnuva so 0)
        // Najprvo zaradi toa sto ima popust cenata kje se namali 400*0.9 = 360, no bidejki barcodot ne zapocnuva so 0
        // nema dopolnitelno sumata da se namali za 30
        assertFalse(SILab2.checkCart(List.of(new Item("Item1", "123456789", 400, 0.9f)), 350));

        // 5 Test case: FALSE (price<=300) && FALSE (discount<=0) && FALSE (barcode ne pocnuva so 0)
        // Prvicnata cena ne se namaluva bidejki discount e 0 i cenata ne se namaluva za 30 bidejki ne se ispolneti site uslovi
        assertFalse(SILab2.checkCart(List.of(new Item("Item1", "123456789", 300, 0)), 280));
    }
}