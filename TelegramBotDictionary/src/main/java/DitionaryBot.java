import lombok.SneakyThrows;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class DitionaryBot extends TelegramLongPollingBot {
    Map<Long, Integer> userMap = new HashMap<>();
    Integer current = 0;

    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(new DitionaryBot());


    }

    @SneakyThrows
    public void onUpdateReceived(Update update) {
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


        }
        userMap.forEach((aLong, integer) -> {
            if (chatId.equals(aLong)) {
                current = integer;
            }
        });
        execute(mainKeyboard(update));
    }
        public SendMessage mainKeyboard (Update update) throws TelegramApiException {

            switch (current) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
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
            row1.add("UZS -> EUR");
            row1.add("UZS -> USD");
            row2.add("USD -> UZS");
            row2.add("EUR -> UZS");
            keybord.add(row1);
            keybord.add(row2);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            String fromUser = update.getMessage().getText();

            return sendMessage;
        }



    public String getBotUsername() {
        return "Yandex_dictionarybot";
    }

    public String getBotToken() {
        return "1547527337:AAE-7ooBh4LG7B3OB0dNcdYkYd5tIEdQwJs";
    }
}