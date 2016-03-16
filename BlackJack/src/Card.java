/**
 * Created by Candyman on 26.02.2016.
 */
public class Card
{
   public String cardValue;
   public static String[] cardValues = {"Двойка", "Тройка", "Четверка", "Пятерка", "Шестерка", "Семерка", "Восьмерка", "Девятка", "Десятка", "Валет", "Дама", "Король", "Туз"};
   public static String[] cardTypes = {"Пик", "Червей", "Бубей", "Треф"};

    public Card(String cardValue)
    {
        this.cardValue = cardValue;
    }
}
