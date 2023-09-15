import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class program {
    static Person person = new Person();

    public static void main(String[] args) {

        /**
         * Сканер создаем в блоке скобках try чтобы автоматически закрылся
         */
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите ваше ФИО, дату ро;дения, номер телефона и пол");
            String text = scanner.nextLine();
            try {
                String[] array = Check_Array(text); // проверяем сколько данных введено. По ТЗ должно быть 6
                array = check_gender(array); // проверяем есть ли одиночный символ, если да то он = ф или м
                array = check_birth(array); // находим элемент массива с двумя точками если нет, то ошиька формата или не найдено
                array = check_phone(array); // проверяем сначала на знаки, потом можем ли перевести из стринг в инт, и также на кол-во цифр в числе иначе выходят ошибки
                check_word_size(array); // проверяем из оставшихся слов ФИО их размерность. Решил что будет не менее 4 букв
                array = check_patronymic(array); // проверяем отчество
                array = check_surname(array); // проверяем фамилию
                add_name(array); // вроде все проверил, заменял после выполнения методов все на Динозавтр так как метод удаления не работал
                //Также в конце каждого метода мы вставляем данные в класс Person далее из него берем данные для записи в файл


            } catch (NotRightLengthArray e ) {
                System.out.println(e.getMessage());
            } catch (LengthArraySymbolWrong e){
                System.out.println(e.getMessage());
            } catch (NotFound e){
                System.out.println(e.getMessage());
            } catch (WrongFormat e){
                System.out.println(e.getMessage());
            }  catch (NotFoundDataOfBirth e){
                System.out.println(e.getMessage());
            }

            try(FileWriter writer = new FileWriter(person.getSurname())){
                writer.write(person.getSentence());
            } catch (IOException e){
                e.getMessage();
            }
            


        }

    }

    public static String[] check_gender(String[] text) {

        String[] myarray = new String[text.length];
        int count = 0;
        String one_symbol = "";
        int index = 0;
        for (int i = 0; i < text.length; i++) {
            if (text[i].length() == 1) {
                count++;
                one_symbol = text[i];
                index=i;
            }
        }

        if (count > 1) {
            throw new LengthArraySymbolWrong("В профиле неправильно указаны данные");

        } else if (count == 0) {
            throw new NotFound("Нет символа f или m");

        } 
        
        if(one_symbol=="f" | one_symbol=="m"){
             person.setGender(one_symbol);
        }
        else{
             throw new NotFound("Нет символа f или m");
        }
        for (int i = 0; i < text.length; i++) {
            if(i == index){
                myarray[i] = "Динозавр"; // у меня почему то функция ремов не работала чтобы я удалил элемент ф или м
            }
            myarray[i]=text[i];
        }
        
        return myarray;
    }
    
    public static String[] check_birth(String[] text){
        String[] myarray = new String[text.length];
        char symbol_birth = '.';
        int count = 0;
        String data_of_birth = "";
        int index = 0;


        for (int j = 0; j<text.length; j++) {
            String str = text[j];
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == symbol_birth) {
                    count++;
                    data_of_birth = str;
                    index = j;
                }
            }
        }

        if(count>2 | count ==1){
            throw new WrongFormat("Неправильный формат даты рождения");
        } 
        else if (count==0){
            throw new NotFoundDataOfBirth("Дата рождения не найдена");
        }
        else{
            person.setBirtg(data_of_birth);
        }

        // text.remove(data_of_birth) не получается выполнить этот код. Почему????

        
        myarray = CopyArray(text);
        
        myarray[index] = "Динозавр";




        return myarray;

    }

    public static String[] check_phone(String[] text){
        String[] myarray = new String[text.length];
        long phone_number = 0;
        int index = 0;
        
        String regexPattern = "-?\\d+(\\.\\d+)?"; // Регулярное выражение для поиска чисел

        for (String str : text) {
            if (str.matches(".*" + regexPattern + ".*")) {
                throw new WrongFormat("Номер не должен содержать знаки");// Ошиббка формата номера
            } 
        }

        for (int i=0; i<text.length; i++) {
            String str = text[i];
            try {
                int number = Integer.parseInt(str);
                phone_number += number;
                index=i;
                break; // Если число найдено, можно выйти из цикла
            } catch (NumberFormatException e) {
                // Проигнорировать элементы, которые нельзя преобразовать в число
            }
        }

        if(phone_number<1000000000 || phone_number > 9999999999L){
            throw new LengthArraySymbolWrong("Телефон должен содержать 10 цифр");

        }
        else{
            person.setPhone(phone_number);
        }
        
        myarray = CopyArray(text);

        for (int i = 0; i < text.length; i++) {
            if(i == index){
                myarray[i] = "Динозавр"; // у меня почему то функция ремов не работала чтобы я удалил элемент ф или м
            }
            myarray[i]=text[i];
        }

        return myarray;


    }

    public static void check_word_size(String[] text){
        for (int i = 0; i < text.length; i++) {
            if (text[i].length() < 4) {
                throw new LengthArraySymbolWrong("Размер данных ФИО должен превышать 4 символа");
            }
        }
    }

    public static String[] check_patronymic(String[] text){
        int index = 0;
        String[] myarray = new String[text.length];
        String patronymic = "";

        for(int i = 0; i<text.length; i++){
            String[] word = text[i].split("");
            for(int j = text[i].length(); j>0;j--){
                if(word[j] == "ч" & word[j-1] == "и" & word[j-2] == "в"){
                    patronymic = text[i];
                    person.setPatronymic(patronymic);
                    index = i;
                    break;

                }
                else if(word[j] == "а" & word[j-1] == "н" & word[j-2] == "в" & word[j-3] == "о"){
                    patronymic = text[i];
                    person.setPatronymic(patronymic);
                    index = i;
                    break;
                }
                else{
                    throw new NotFound("Не найдено отчество");// ошибка не найдено отчество
                }

            }

        }


        for (int i = 0; i < text.length; i++) {
            if(i == index){
                myarray[i] = "Динозавр"; 
            }
            myarray[i]=text[i];
        }

        return myarray;

    }

     public static String[] check_surname(String[] text){
        int index = 0;
        String[] myarray = new String[text.length];
        String surname = "";

        for(int i = 0; i<text.length; i++){
            String[] word = text[i].split("");
            for(int j = text[i].length(); j>0;j--){
                if((word[j] == "о" & word[j-1] == "в") | (word[j] == "е" & word[j-1] == "в") | (word[j] == "и" & word[j-1] == "н") | (word[j] == "ы" & word[j-1] == "н")){
                    surname = text[i];
                    person.setSurname(surname);
                    index = i;
                    break;

                }
                else if((word[j] == "о" & word[j-1] == "в" & word[j-2]=="a") | (word[j] == "е" & word[j-1] == "в" & word[j-2]=="a") | (word[j] == "и" & word[j-1] == "н" & word[j-2]=="a") | (word[j] == "ы" & word[j-1] == "н" & word[j-2]=="a")){
                    surname = text[i];
                    person.setSurname(surname);
                    index = i;
                    break;
                }
                else if((word[j] == "й" & word[j-1] == "и") | (word[j] == "й" & word[j-1] == "ы") | (word[j] == "й" & word[j-1] == "о") | (word[j] == "я" & word[j-1] == "а")){
                    surname = text[i];
                    person.setSurname(surname);
                    index = i;
                    break;

                }
                else{
                    throw new NotFound("Не найдено фамилия");// ошибка не найдено отчество
                }

            }

        }


        for (int i = 0; i < text.length; i++) {
            if(i == index){
                myarray[i] = "Динозавр"; 
            }
            myarray[i]=text[i];
        }

        return myarray;

    }

    public static void add_name(String[] text){
        for (int i = 0; i < text.length; i++) {
            if(text[i] != "Динозавр"){
                person.setName(text[i]); 
            }
        }
    }


    public static String[] CopyArray(String[] array){
    String[] myarray = new String[array.length];
    for (int i = 0; i < array.length; i++) {
            
            myarray[i]=array[i];
        }
        return myarray;
}
    
     

    static String[] Check_Array(String text) {
        String[] myarray = text.split(" ");
        if (myarray.length < 6) {
            throw new NotRightLengthArray("Кол-во введенных данных недостаточно");

        } else if (myarray.length > 6) {
            throw new NotRightLengthArray("Количество введенных данных больше чем нуЖно");
        }

        return myarray;

    }

}

class NotRightLengthArray extends RuntimeException {

    public NotRightLengthArray(String message) {
        super(message);
    }

}

class NotFound extends RuntimeException {
    public NotFound(String message) {
        super(message);
    }
}

class LengthArraySymbolWrong extends RuntimeException {
    public LengthArraySymbolWrong(String message) {
        super(message);
    }
}

class WrongFormat extends RuntimeException {
    public WrongFormat(String message) {
        super(message);
    }
}

class NotFoundDataOfBirth extends RuntimeException {
    public NotFoundDataOfBirth(String message) {
        super(message);
    }
}



class Person {
    private String name;
    private String surname;
    private String patronymic;
    private String birtg;
    private Long phone;
    private char gender;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getBirtg() {
        return birtg;
    }

    public Long getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setBirtg(String birtg) {
        this.birtg = birtg;
    }

    public void setPhone(long phone_number) {
        this.phone = phone_number;
    }

    public void setGender(String text_gender) {
        this.gender = text_gender.charAt(0);
    }

    public char getGender() {
        return gender;
    }

    public String getSentence(){
        return "<"+surname+">"+"<"+name+">"+"<"+patronymic+">"+"<"+birtg+">"+"<"+phone+">"+"<"+gender+">";
    }


}