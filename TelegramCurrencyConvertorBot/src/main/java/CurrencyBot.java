import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


class CurrencyBot extends TelegramLongPollingBot {
    Double usd;
    Double eur;
    Double byn;
    Map<Long, Integer> userMap = new HashMap<>();
    Integer current = 0;

    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(new CurrencyBot ());

    }

    @SneakyThrows
    public void onUpdateReceived(Update update) {
        for (Currency currency : currencies()) {
            if (currency.getCcy().equalsIgnoreCase("eur")) {
                this.eur = currency.getRate();
            }
            if (currency.getCcy().equalsIgnoreCase("usd")) {
                this.usd = currency.getRate();
            }
            if (currency.getCcy().equalsIgnoreCase("byn")) {
                this.byn = currency.getRate();
            }
        }
        Long chatId = update.getMessage().getChatId();
        String fromUser = update.getMessage().getText();
        userMap.putIfAbsent(chatId, 0);


        switch (fromUser) {
            case "/start":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 0);
                        this.current = integer;
                    }
                });
                break;
            case "UZ So`m -> Euro":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 1);
                        this.current = integer;
                    }
                });
                break;
            case "UZ So`m -> US Dollar":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 2);
                        this.current = integer;
                    }
                });
                break;
            case "US Dollar -> UZ So`m":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 3);
                        this.current = integer;
                    }
                });
                break;
            case "Euro -> UZ So`m":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 4);
                        this.current = integer;
                    }
                });
                break;
            case "Bellarusian ruble -> UZ So`m":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 5);
                        this.current = integer;
                    }
                });
                break;
            case "UZ So`m -> Bellarusian ruble":
                userMap.forEach((aLong, integer) -> {
                    if (chatId.equals(aLong)) {
                        userMap.put(chatId, 6);
                        this.current = integer;
                    }
                });
                break;
        }
        userMap.forEach((aLong, integer) -> {
            if (chatId.equals(aLong)) {
                current = integer;
            }
        });


        execute(mainKeyboard(update));
    }

    public String getBotToken() {
        return "1370838407:AAH1Vq3_r3M5_8UF9phw-lsJHkroyUWl1wo";
    }

    public String getBotUsername() {
        return "Currencyconvertorbot";
    }

    public SendMessage mainKeyboard(Update update) throws TelegramApiException {
        switch (current) {
            case 1:
                execute(uzsEur(update));
                break;
            case 2:
                execute(uzsUsd(update));
                break;
            case 3:
                execute(usdUzs(update));
                break;
            case 4:
                execute(eurUzs(update));
                break;
            case 5:
                execute(bynUzs(update));
                break;
            case 6:
                execute(uzsByn(update));
                break;
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keybord = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keybord);
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("UZ So`m -> Euro");
        row1.add("UZ So`m -> US Dollar");
        row1.add("UZ So`m -> Bellarusian ruble");
        row2.add("Euro -> UZ So`m");
        row2.add("US Dollar -> UZ So`m");
        row2.add("Bellarusian ruble -> UZ So`m");
        keybord.add(row1);
        keybord.add(row2);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        String fromUser = update.getMessage().getText();
        if (fromUser.equals("/start")) {
            String userFirstName = update.getMessage().getChat().getFirstName();
            sendMessage.setText("Welcome" + userFirstName + " " + "\nThis bot can convert money from" +
                    " Uz So`m to Euro, US Dollar, Bellarusian ruble and back" + "\nChoose the currency and than enter" +
                    " the amount");
        }
        if (fromUser.equals("/help")){
            sendMessage.setText("This bot is made to convert money from Uz So`m to Euro, US Dollar, Bellarusian ruble and back." +
                    " All you need to do is " +
                    "choose one button below and enter amount that you want to trasfer." +
                    " \nBot will convert money only from Uzbek so`m to other currencies and back!!!" +
                    "\nЭтот бот предназначен для конвертации денег из узбекской валюты в евро, доллары США, " +
                    "белорусские рубли и обратно. Все, что вам нужно сделать, это нажать одну кнопку ниже и ввести сумму которую вы хотите конвертироват.\n" +
                    "Бот будет конвертировать деньги только с узбекской валюты в другие валюты и обратно !!!" +
                    "Ushbu bot O`zbek so`m dan Evro, AQSh Dollari, Belorusiya rubli va orqaga o'tkazish uchun yaratilgan bo'lib," +
                    " sizga quyida bitta tugmachani tanlash va o'zingizga kerakli pulni kiritish kifoya.\n" +
                    "Bot pulni faqat o'zbek so`m-dan boshqa valyutalarga va orqaga o'zgartiradi !!!");
        }
        return sendMessage;
    }

    public SendMessage uzsEur(Update update) {
        SendMessage sendMessage = new SendMessage();
        Double amount = Double.parseDouble(update.getMessage().getText());
        if (amount > 0) {
            double result = amount / eur;
            sendMessage.setText(result + " Euro");
        } else {
            sendMessage.setText("Amount should be more 0!!!");
        }
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }

    public SendMessage uzsUsd(Update update) {
        SendMessage sendMessage = new SendMessage();
        Double amount = Double.parseDouble(update.getMessage().getText());
        if (amount > 0) {
            double result = amount / usd;
            sendMessage.setText(result + " United States Dollars");
        } else {
            sendMessage.setText("Amount should be more 0!!!");
        }
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }

    public SendMessage uzsByn(Update update) {
        SendMessage sendMessage = new SendMessage();
        Double amount = Double.parseDouble(update.getMessage().getText());
        if (amount > 0) {
            double result = amount / byn;
            sendMessage.setText(result + " United States Dollars");
        } else {
            sendMessage.setText("Amount should be more 0!!!");
        }
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }


    public SendMessage eurUzs(Update update) {
        SendMessage sendMessage = new SendMessage();
        Double amount = Double.parseDouble(update.getMessage().getText());
        if (amount > 0) {
            double result = amount * eur;
            sendMessage.setText(result + " so`m");
        } else {
            sendMessage.setText("Amount should be more 0!!!");
        }
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }

    public SendMessage usdUzs(Update update) {
        SendMessage sendMessage = new SendMessage();
        Double amount = Double.parseDouble(update.getMessage().getText());
        if (amount > 0) {
            double result = amount * usd;
            sendMessage.setText(result + " so`m");
        } else {
            sendMessage.setText("Amount should be more 0!!!");
        }
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }

    public SendMessage bynUzs(Update update) {
        SendMessage sendMessage = new SendMessage();
        Double amount = Double.parseDouble(update.getMessage().getText());
        if (amount > 0) {
            double result = amount * byn;
            sendMessage.setText(result + " so`m");
        } else {
            sendMessage.setText("Amount should be more 0!!!");
        }
        sendMessage.setChatId(update.getMessage().getChatId());
        return sendMessage;
    }

    public ArrayList<Currency> currencies() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");
        URLConnection urlConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        Type type = new TypeToken<ArrayList<Currency>>(){}.getType();
        return gson.fromJson(bufferedReader, type);
    }

}


@Getter
@Setter
@ToString
class Currency {
    Integer id;
    Integer Code;
    String Ccy;
    String CcyNm_RU;
    String CcyNm_UZ;
    String CcyNm_UZC;
    String CcyNm_EN;
    Double Nominal;
    Double Rate;
    String Diff;
    String Date;

}










