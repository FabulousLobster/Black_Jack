import com.sun.deploy.util.ArrayUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Random;

public class BlackJack {
    public static int startCapital = 1000;
    public static ArrayList<Card> cards = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        GameRules();
        gameProcess(startCapital);
    }

    public static void GameRules() throws IOException {
        System.out.println("В этом варианте Блек Джека используется одна колода карт.");
        System.out.println("Игроки делают ставки, кладя фишки на соответствующие поля игрового стола. Ставки делаются до раздачи карт.");
        System.out.println("После того, как первая карта сдана, игроку запрещается делать ставку, однако он может ее удвоить один раз за игру.");
        System.out.println("Значения очков каждой карты: от двойки до десятки — соответственно от 2 до 10, у туза — 1 или 11 (11 пока общая сумма не больше 21, далее 1), у т. н. картинок (король, дама, валет) — 10.");
        System.out.println("В случае, если игрок набрал 21 очко тремя семерками, ему выплачивается ставка в бонусном размере.");

    }

    // Создаем колоду карт.
    public static ArrayList<String> deckOfCards() {
//        for(int i = 0; i < Card.cardValues.length; i++)
//        {
//            for(int j = 0; j < Card.cardTypes.length; j++)
//            {
//                cards.add(new Card(Card.cardValues[i] + " " + Card.cardTypes[j]));
//            }
//
//        }


        ArrayList<String> cardValue = new ArrayList<String>(Arrays.asList("Двойка", "Тройка", "Четверка", "Пятерка", "Шестерка", "Семерка", "Восьмерка", "Девятка", "Десятка", "Валет", "Дама", "Король", "Туз"));
        ArrayList<String> spadesDeck = new ArrayList<String>();
        ArrayList<String> heartsDeck = new ArrayList<String>();
        ArrayList<String> diamondsDeck = new ArrayList<String>();
        ArrayList<String> clubsDeck = new ArrayList<String>();
        String spades = "Пик";
        String hearts = "Червей";
        String diamonds = "Бубей";
        String clubs = "Треф";
        for (int i = 0; i < cardValue.size(); i++) {
            spadesDeck.add(i, cardValue.get(i) + " " + spades);
            heartsDeck.add(i, cardValue.get(i) + " " + hearts);
            diamondsDeck.add(i, cardValue.get(i) + " " + diamonds);
            clubsDeck.add(i, cardValue.get(i) + " " + clubs);
        }
        ArrayList<String> deckOfCards = new ArrayList<String>();
        deckOfCards.addAll(spadesDeck);
        deckOfCards.addAll(diamondsDeck);
        deckOfCards.addAll(heartsDeck);
        deckOfCards.addAll(clubsDeck);

        return deckOfCards;
    }

    //Рандомизатор, добавляющий случайные карты из колоды в массивы карт дилера или игрока.
    private static ArrayList<String> cardRandomizer(int z, ArrayList<String> playerOrDealerCards, ArrayList<String> deckOfCards)
    {
        Random random = new Random();
        for (int i = 0; i < z; i++) {
            int x = random.nextInt(deckOfCards.size());
            String randomCard = cards.get(x).cardValue;
            deckOfCards.remove(randomCard);
            playerOrDealerCards.add(randomCard);
        }
        return playerOrDealerCards;
    }

    //Проверка на соответствие ставки, внесенной игроком наложенным ограничениям;
    //В случае принятия ставки возвращается баланс после ее вычета.
    public static ArrayList<Integer> moneyAfterBetsChecking(int bet, int money) throws IOException {
        ArrayList<Integer> moneyAndBetValuesAfterBetsChecking = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        if (bet >= 10 && bet <= 500 && bet <= money)
        {
            money -= bet;
            moneyAndBetValuesAfterBetsChecking.add(0, bet);
            moneyAndBetValuesAfterBetsChecking.add(1, money);
        }
        outer: if (bet < 10 || bet > 500 || bet > money)
        {
            for (;;)
            {
                System.out.println("Минимальная ставка - 10. Максимальная ставка - 500. Ставка не должна превышать текущий баланс.");
                System.out.println("Введите, пожалуйста, приемлемую сумму: ");
                int betNext = Integer.parseInt(reader.readLine());
                if (betNext >= 10 && betNext <= 500 && betNext <= money)
                {
                    money -= betNext;
                    moneyAndBetValuesAfterBetsChecking.add(0, betNext);
                    moneyAndBetValuesAfterBetsChecking.add(1, money);
                    break outer;
                }

            }
        }
        return moneyAndBetValuesAfterBetsChecking;
    }

    private static int pointsCounting(int points, String z)
    {
        if (z.contains("Двойка"))
            points += 2;
        else if (z.contains("Тройка"))
            points += 3;
        else if (z.contains("Четверка"))
            points += 4;
        else if (z.contains("Пятерка"))
            points += 5;
        else if (z.contains("Шестерка"))
            points += 6;
        else if (z.contains("Семерка"))
            points += 7;
        else if (z.contains("Восьмерка"))
            points += 8;
        else if (z.contains("Девятка"))
            points += 9;
        else if (z.contains("Десятка") || z.contains("Валет") || z.contains("Дама") || z.contains("Король"))
            points += 10;
        else if (z.contains("Туз"))
            points += 11;

        return points;
    }

    public static int gameProcess(int startCapital) throws IOException {
        if (startCapital < 10)
        {
            System.out.println("Вы проиграли, недостаточно средств для продолжения игры.");
            return 0;
        }
        System.out.println("");
        System.out.println("Текущий баланс: " + startCapital);
        System.out.println("Сделайте вашу ставку. Минимальная ставка - 10. Максимальная ставка - 500.");
        ArrayList<String> deckOfCards = deckOfCards();
        ArrayList<String> givenCards = new ArrayList<String>();
        ArrayList<String> dealerCards = new ArrayList<>();
        int playerPoints = 0;
        int dealerPoints = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int bet = Integer.parseInt(reader.readLine());
        bet = moneyAfterBetsChecking(bet, startCapital).get(0);
        int money = moneyAfterBetsChecking(bet, startCapital).get(1);
        System.out.println("Текущий баланс: " + money + ". Ваша ставка: " + bet);
        int realBet = bet; // Из-за обилия переменных с разными именами вводятся переменные realBet и realMoney для унификации и удобочитаемости кода.
        int realMoney = money;

        givenCards = cardRandomizer(2, givenCards, deckOfCards);
//        deckOfCards = optimizationForDeckOfCards(deckOfCards, givenCards);
        dealerCards = cardRandomizer(1, dealerCards, deckOfCards);
//        deckOfCards = optimizationForDeckOfCards(deckOfCards, dealerCards);

        System.out.println("Ваши карты:");
        for (int i = 0; i < 2; i++)
            System.out.println(givenCards.get(i));

        for (int i = 0; i < givenCards.size(); i++)
            playerPoints = pointsCounting(playerPoints, givenCards.get(i));
        System.out.println("Ваши очки равны: " + playerPoints);
        System.out.println("");
        System.out.println("Карты дилера:");
        System.out.println(dealerCards.get(0));
        dealerPoints = pointsCounting(dealerPoints, dealerCards.get(0));
        System.out.println("Вторая карта дилера играется в закрытую.");
        System.out.println("Очки дилера равны: " + dealerPoints);
        System.out.println("");

        for (; bet <= money ;)
        {
            System.out.println("Удвоить ставку?(Да/Нет)");
            String doubleBetAnswer = reader.readLine();
            if (doubleBetAnswer.equals("Да") || doubleBetAnswer.equals("да"))
            {
                int doubleBet = bet * 2;
                int doubledMoney = money - bet;
                System.out.println("Текущий баланс: " + doubledMoney + ". Ваша ставка: " + doubleBet);
                realBet = doubleBet;
                realMoney = doubledMoney;
                break;
            }
            if (doubleBetAnswer.equals("Нет") || doubleBetAnswer.equals("нет"))
            {
                realBet = bet;
                realMoney = money;
                break;
            }
        }

        for (; playerPoints < 21; ) {
            System.out.println("Хотите взять еще карту?(Да/Нет)");
                String moreCardsAnswer = reader.readLine();
                if (moreCardsAnswer.equals("Да") || moreCardsAnswer.equals("да")) {
                    givenCards = cardRandomizer(1, givenCards, deckOfCards);
//                    deckOfCards = optimizationForDeckOfCards(deckOfCards, givenCards);
                    System.out.println("Ваши карты: ");
                    for (int i = givenCards.size() - 1; i < givenCards.size(); i++)
                        playerPoints = pointsCounting(playerPoints, givenCards.get(i));

                    for (int i = 0; i < givenCards.size(); i++)
                        System.out.println(givenCards.get(i));
                    System.out.println("Ваши очки равны: " + playerPoints);
                    System.out.println("");
                }
                if (moreCardsAnswer.equals("Нет") || moreCardsAnswer.equals("нет")) {

                    for (; dealerPoints < 17; )
                    {
                        dealerCards = cardRandomizer(1, dealerCards, deckOfCards);
//                        deckOfCards = optimizationForDeckOfCards(deckOfCards, dealerCards);
                        for (int i = dealerCards.size() - 1; i < dealerCards.size(); i++)
                            dealerPoints = pointsCounting(dealerPoints, dealerCards.get(i));
                    }
                    if (dealerPoints >= 17 && dealerPoints <= 21)
                    {
                        if (playerPoints == dealerPoints)
                        {
                            System.out.println("Карты дилера:");
                            for (int i = 0; i < dealerCards.size(); i++)
                                System.out.println(dealerCards.get(i));
                            System.out.println("Дилер набрал достаточно карт. Очки дилера составляют:" + dealerPoints);
                            realMoney = realMoney + realBet;
                            System.out.println("Количество набранных очков совпадает с количеством очков дилера.");
                            System.out.println("Ваш баланс составляет " + realMoney);
                            gameProcess(realMoney);
                            return realMoney;
                        }
                        if (dealerPoints > playerPoints) {
                            System.out.println("Карты дилера:");
                            for (int i = 0; i < dealerCards.size(); i++)
                                System.out.println(dealerCards.get(i));
                            System.out.println("Дилер победил! Очки дилера составляют: " + dealerPoints);
                            System.out.println("Ваш баланс составляет " + realMoney);
                            gameProcess(realMoney);
                            return realMoney;
                        }

                    }

                    if (dealerPoints > 21 || playerPoints > dealerPoints) {
                        System.out.println("Карты дилера:");
                        for (int i = 0; i < dealerCards.size(); i++)
                            System.out.println(dealerCards.get(i));
                        realMoney = realMoney + realBet * 2;
                        System.out.println("Дилер проиграл. Очки дилера составляют: " + dealerPoints + ". Вы победили!");
                        System.out.println("Ваш баланс составляет " + realMoney);
                        gameProcess(realMoney);
                        return realMoney;
                    }

                }
        }


        if (playerPoints == 21)
        {
            if (givenCards.get(0).contains("Семерка") && givenCards.get(1).contains("Семерка") && givenCards.get(2).contains("Семерка"))
            {
                System.out.println("Карты дилера:");
                for (int i = 0; i < dealerCards.size(); i++)
                    System.out.println(dealerCards.get(i));
                System.out.println("Дилер набрал достаточно карт. Очки дилера составляют:" + dealerPoints);
                realMoney = realMoney + (realBet * 2) * 2;
                System.out.println("Блек-Джек! Поздравляю, вы победили!");
                System.out.println("Ваш баланс составляет " + realMoney);
                gameProcess(realMoney);
                return realMoney;
            }
            System.out.println("Карты дилера:");
            for (int i = 0; i < dealerCards.size(); i++)
                System.out.println(dealerCards.get(i));
            System.out.println("Дилер набрал достаточно карт. Очки дилера составляют:" + dealerPoints);
            realMoney = realMoney + realBet * 2;
            System.out.println("Блек-Джек! Поздравляю, вы победили!");
            System.out.println("Ваш баланс составляет " + realMoney);
            gameProcess(realMoney);
            return realMoney;
        }

        if (playerPoints > 21)
        {
            System.out.println("Карты дилера:");
            for (int i = 0; i < dealerCards.size(); i++)
                System.out.println(dealerCards.get(i));
            System.out.println("Дилер набрал достаточно карт. Очки дилера составляют:" + dealerPoints);
            System.out.println("К сожалению, вы проиграли.");
            System.out.println("Ваш баланс составляет " + realMoney);
            gameProcess(realMoney);
            return realMoney;
        }

        return 0;
    }









}