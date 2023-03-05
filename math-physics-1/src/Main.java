import java.util.Scanner;
import java.lang.Math;

public class Main {

    public static long factorialUsingRecursion(int n) { //рекурсивная функция факториала, используется для Сочетания из n по k
        if (n==0 || n==1) {
            return 1;
        }
        return n * factorialUsingRecursion(n - 1);
    }

//    public static double koeff_even (int n) {
//        return factorialUsingRecursion (n)/(factorialUsingRecursion(n/2)*factorialUsingRecursion(n-n/2))/Math.pow(2,n);
//    }

    public static String[] parse_to_array(String s){//парсим строку из "x^2-5y+3" в массив: [+x^2, -5y, +3]
        String[] final_string = null;
        char first_sign;//первый знак в строке
        String replace_s = "";//строка, в которой заменим все знаки на @-/@+, чтобы потом обрезать строку по @
        if (s.toCharArray()[0] == '-') {//если первый символ в строке минус, то
            first_sign = '-';//запоминаем его
            replace_s = s.replaceFirst("-", "");//и заменяем первое вхождение на пустое пространство (нужно для корректной работы replaceAll)
        }
        else {//если первый символ в строке не знак, то это, очевидно, положительное число
            first_sign = '+';//поэтому запоминаем знак +
            replace_s = s;//и строка остается неизменной
        }
        replace_s = replace_s.replaceAll("-", "@-").replaceAll("\\+", "@+");//добавляем спец разделитель перед знаками, чтобы сохранить переменную вместе с ее знаком
        replace_s = first_sign + replace_s;//добавляем первый "потерянный" знак, который определили в if
        final_string = replace_s.split("@");//создаем массив с помощью разделения по @
        return final_string;
    }
 //DON'T USE узнаем максимальную степень, чтобы установить размер массива
    public static int parse (String s) {
        char[] letters = s.toCharArray();
        String max_power = "";
        int result = 0;
        for (int i = 0; i < letters.length; i++) {
            if (letters[i]=='^') {
                int k = i+1;
                    while (k<letters.length && Character.isDigit(letters[k])){
                        max_power+=letters[k];
                        k++;
                    }
                    if (Integer.parseInt(max_power)>result) {
                        result = Integer.parseInt(max_power);
                        max_power = "";
                    }
            }
            if ((letters[i]=='x'||letters[i]=='y')  && (i == letters.length-1 || letters[i+1]!='^') && (result==0)) {
                result = 1;
            }
        }
        return result;
    }
// DON'T USE устаревшая версия парсера
    public static double[][] getArrayKoeff (String s, int maxpower) {
        char[] letters = s.toCharArray();
        String max_power = "0";
        double[][] result = new double[2][maxpower+1];
        for (int i = 0; i < letters.length; i++) {
//            if ((letters[i] == '-') && ((letters[i+1] != '^') || (letters[i+1] != '-') || (letters[i+1] != '+'))) {
//                if (letters [i+1] == 'x') {
//                    if (letters[i+2] == '^') {
//                        int k = i+1;
//                        while (k<letters.length && (Character.isDigit(letters[k]) || letters[k]=='.')){
//                            max_power+=letters[k];
//                            k++;
//                        }
//                        result[0][Integer.parseInt(max_power)] = -1;
//                        max_power = "0";
//                    }
//                    else if ((letters[i+2]=='+') || (letters[i+2]=='-')) {
//                        break;
//                    }
//                }
//            }

            if (letters[i]=='^' ) {
                int k = i+1;
                while (k<letters.length && (Character.isDigit(letters[k]) || letters[k]=='.')){
                    max_power+=letters[k];
                    k++;
                }


                String figure = "";
                k = i-2;
                while (k>=0 && (Character.isDigit(letters[k]) || letters[k]=='.' || letters[k]=='-')){
                    figure = letters[k]+figure;
                    if(letters[k]=='-'){
                        break;
                    }
                    k--;
                }
                if(figure.length() == 0 ){
                    figure = "1";
                }
                if(figure.equalsIgnoreCase("-") ){
                    figure = "-1";
                }
                if (letters[i-1]=='x') {
                    result[0][Integer.parseInt(max_power)] = Double.parseDouble(figure);
                }
                if (letters[i-1]=='y') {
                    result[1][Integer.parseInt(max_power)] = Double.parseDouble(figure);
                }
                max_power = "";
            }
            if (Character.isDigit(letters[i]) ) {
                int k = i;
                String figure = "0";
                if (i==0) {
                    while (k < letters.length && (Character.isDigit(letters[k]) || letters[k]=='.')) {
                        figure += Character.toString(letters[k]);
                        k++;
                    }
                    if (letters[k]=='x' || letters[k]=='y') {
                        figure = "0";
                    }
                    else {
                        result[0][0] = Double.parseDouble(figure);
                        result[1][0] = Double.parseDouble(figure);
                    }

                }
                else {
                    if (letters[i-1]=='^') {
                        figure = "0";
                    }
                    if (letters[i-1]=='-') {
                        while (k < letters.length && (Character.isDigit(letters[k]) || letters[k]=='.')) {
                            figure += Character.toString(letters[k]);
                            k++;
                        }
                        if (k < letters.length-1 && (letters[k]=='x' || letters[k]=='y')) {
                            figure = "0";
                        }
                        else {
                            result[0][0] = Double.parseDouble(figure)*(-1);
                            result[1][0] = Double.parseDouble(figure)*(-1);
                        }
                    }
                    if (letters[i-1]=='+') {
                        while (k < letters.length && (Character.isDigit(letters[k]) || letters[k]=='.')) {
                            figure += Character.toString(letters[k]);
                            k++;
                        }
                        if (k < letters.length-1 && (letters[k]=='x' || letters[k]=='y')) {
                            figure = "0";
                        }
                        else {
                            result[0][0] = Double.parseDouble(figure);
                            result[1][0] = Double.parseDouble(figure);
                        }
                    }
                }

            }

//            if ((letters[i]=='x'||letters[i]=='y')  && (i == letters.length-1 || letters[i+1]!='^') && (result==0)) {
//                result = 1;
//            }
        }
        return result;
    }

    public static double[] koeff_cos_even (int n) {//формула понижения для четной степени косинуса (в разложении всегда будут косинусы)
        double[] result = new double [n+1];//размерность конечного массива - степень+1, так как в разложении [const, 0, cos2phi, 0, cos4phi...], т.е. для cos^4phi будут const, cos2phi, cos4phi, а на нечет местах будут нули, т.е. длина массива = 5
        result [0] = factorialUsingRecursion(n)/(factorialUsingRecursion(n/2)*factorialUsingRecursion(n-n/2)) / Math.pow(2, n);//const = (C из n по n/2)/2^n
        int even = 0;//счетчик, чтобы попадать только на четные места
        for (int i = result.length-1; i > 0; i--) {//идем в обратную сторону, т. к. надо начинать с четного места и нулевое место уже забито
            if (i%2!=0) {
                result[i] = 0;
            }
            else {// (C из n по even)/2^(n-1)
                result[i] = (factorialUsingRecursion(n)/(factorialUsingRecursion(even)*factorialUsingRecursion(n-even)))/Math.pow(2,n-1);
                even++;//счетчик четных итераций
            }
        }
        return result;
    }

    public static double[] koeff_cos_odd (int n) {//формула понижения для нечетной степени косинуса (в разложении всегда будут косинусы)
        double[] result = new double [n+1];//например, для cos^3 -> [0, 0.75, 0, 0.25] размерность степень исх + 1, коэфф всегда будут только на нечетных местах
        result [0] = 0;//на нулевом (т.е. четном месте) всегда будет 0
        int odd = 0;//счетчик для нечетных итераций
        for (int i = result.length-1; i > 0; i--) {//идем в обратную сторону
            if (i%2==0) {//если четное место, то 0
                result[i] = 0;
            }
            else {//(C из n по odd)/2^(n-1)
                result[i] = (factorialUsingRecursion(n)/(factorialUsingRecursion(odd)*factorialUsingRecursion(n-odd)))/Math.pow(2,n-1);
                odd++;
            }
        }
        return result;
    }

    public static double[] koeff_sin_even (int n) {//понижение четной степени для синуса (в разложении всегда будут косинусы)
        double[] result = new double [n+1];// sin^2 -> [0.5,0,-0.5] => 0.5-0.5*cos(2phi), размерность = степень + 1, коэфф всегда на четных местах
        result [0] = factorialUsingRecursion(n)/(factorialUsingRecursion(n/2)*factorialUsingRecursion(n-n/2)) / Math.pow(2, n); // const = (C из n по n/2)/2^n
        int even = 0;//счетчик четных итераций
        for (int i = result.length-1; i > 0; i--) {
            if (i%2!=0) {//если нечетное место, то коэфф = 0
                result[i] = 0;
            }
            else {//в формуле появляется периодический минус: (-1)^(n/2 - even) * (C из n по even)/2^(n-1)
                result[i] = Math.pow(-1, n/2-even)*factorialUsingRecursion(n)/(factorialUsingRecursion(even)*factorialUsingRecursion(n-even))/Math.pow(2,n-1);
                even++;
            }
        }
        return result;
    }

//    ТОЛЬКО ЗДЕСЬ КОЭФФ ДЛЯ СИНУСОВ
    public static double[] koeff_sin_odd (int n) {//понижение нечетной степени для синуса (в разложении всегда будут СИНУСЫ)
        double[] result = new double [n+1];// sin^3 -> [0, 0.75, 0, -0.25] => 0.75*sin(phi) - 0.25*sin(3phi), размерность = степень+1, коэфф только на нечетных местах
        result [0] = 0;//нулевой элемент всегда = 0, так как он четный
        int odd = 0;//счетчик нечетных итераций
        for (int i = result.length-1; i > 0; i--) {
            if (i%2==0) {//если позиция четная, то коэфф = 0
                result[i] = 0;
            }
            else {// (-1)^( (n-1)/2 - odd) * (C из n по odd)/2^(n-1)
                result[i] = Math.pow(-1, (n-1)/2-odd)*factorialUsingRecursion(n)/(factorialUsingRecursion(odd)*factorialUsingRecursion(n-odd))/Math.pow(2,n-1);
                odd++;
            }
        }
        return result;
    }

    public static double[] mixed_multy (double koeff,int n, int m) {//возвращаем коэфф при смешанном произведении в разложении которого только косинусы (случай когда при x/cos любая степень, а при y/sin четная)
        double[] result = new double[n+m+1];//xy^2 -> cos(phi)*sin(phi)^2 -> [0, 0.25, 0, -0.25], размерность = 1(степень при cos)+2(степень при sin)+1
        double[] array_cos = null;//массив с коэфф разложения косинуса
        if(n%2 != 0) {//если степень при косинусе нечетная, то используем функцию понижения для нечетных степеней косинуса
            array_cos = koeff_cos_odd(n);
        }else{//иначе используем функция понижения степени для четных степеней косинуса
            array_cos = koeff_cos_even(n);
        }

//        for (int i =0; i < array_cos.length; i++) {
//            System.out.println(array_cos[i]);
//        }
//
//        System.out.println("\nSin");
        double[] array_sin_even = koeff_sin_even(m);//массив с коэфф разложения синуса (так как его степень четная, то он раскладывается по косинусам)

//        for (int i =0; i < array_sin_even.length; i++) {
//            System.out.println(array_sin_even[i]);
//        }
//        System.out.println("\nRes");
        //используем формулу умножения cos(a) * cos(b) = 1/2 * (cos(a-b) + cos(a+b)) (понижали степени у тригонометрических функций, чтобы ее использовать)
        //рассмотрим два примера:    x^2*y^2                             &&                     x*y^4:
        //                [0.5, 0, 0.5] * [0.5, 0, -0.5]                          [0, 1, 0, 0, 0] * [0.375, 0, -0.5, 0, 0.125]
        //                res[0+0] = 0.5 * 0.5 * 0.5 = 0.125                      res[0+j] == 0 всегда
        //                res[0+1] = 0.5 * 0.5 * 0 = 0
        //                res[0+2] = 0.5 * 0.5 * (-0.5) = -0.125
        //                res[1+j] == 0 всегда                                    res[1+4] = 0.5 * 0.125 - крайний элемент на 5-ом месте (размерность массива = 6)
        //                res[2+0] = 0.125 НО на 2 месте уже было -0.125,
        //                т.е. вместе они дают 0; res[2+1] == 0                   res = [0, 1/8, 0, -3/16, 0, 1/16]
        //                res[2+2] = -0.125 - крайний эелемент на 4-ом месте
        //т.е. коэфф для смешанного произведения, которое раскладывается
        //только по косинусам: [0.125, 0, 0, 0, -0.125] = 1/8-1/8cos(4phi)
        for (int i = 0; i < array_cos.length; i++) {//проходим по массиву коэфф от х
            for(int j = 0; j < array_sin_even.length; j++) {//проходим по массиву коэфф от у
                result[i+j]+=0.5*array_cos[i]*array_sin_even[j];//в элемент массива с суммой индексов массивов кос и син записываем полусумму коэфф
                result[Math.abs(i-j)]+=0.5*array_cos[i]*array_sin_even[j];//в элемент массива с разностью индексов массивов кос и син записываем полуразность коэфф
                //System.out.println("i: "+i+"  j: "+j+" r "+0.5*array_cos[i]*array_sin_even[j]);
            }
        }

        for (int i =0; i < result.length; i++) {
            System.out.println(result[i]);
        }

        return result;
    }

    public static double[] mixed_multy_sin (double koeff,int n, int m) {//получаем коэфф смешанного произв в разложении которого всегда синусы (т.е. когда степень х любая, а степень у - нечетная)
        double[] result = new double[n+m+1];//cos(phi) * sin(phi) -> [0,0,0.5] -> 0.5*sin(2phi)
        double[] array_cos = null;//массив с коэфф разложения косинуса
        if(n%2 != 0) {//если степень при косинусе нечетная, то используем функцию понижения для нечетных степеней косинуса
            array_cos = koeff_cos_odd(n);
        }else{//иначе используем функция понижения степени для четных степеней косинуса
            array_cos = koeff_cos_even(n);
        }

//        for (int i =0; i < array_cos.length; i++) {
//            System.out.println(array_cos[i]);
//        }

       // System.out.println("\nSin");
        double[] array_sin_odd = koeff_sin_odd(m);//разложение у по нечетной степени, что всегда дает разложение по синусам

//        for (int i =0; i < array_sin_odd.length; i++) {
//            System.out.println(array_sin_odd[i]);
//        }
//        System.out.println("\nRes");
        //понижали степень, чтобы использовать формулу умножения sin(a) * cos(b) = 0.5 (sin(a-b) + sin(a+b))
        // cos(phi)^2*sin(phi)^3 -> cosphi: [0.5, 0, 0.5, 0] (дополнили нулем в конце), sinphi: [0, 0.75, 0, -0.25]
        // res[0+0]=res[1+1]=res[2+2]=res[3+3]==0
        //так как синус нечетная функция, то имеет значения, вычитать ли а-б или б-а, так как появляется отриц знак
        //(1/2cos(2phi)/1/2)*(3/4*sin(phi)-1/4*sin(3phi))=3/8sin(phi)-1/8sin(3phi)+
        //+3/8sin(phi)*cos*2phi) = 3/16 (-sin(phi)+sin(3phi))
        //-1/8sin(3phi_*cos(2phi) = -1/16(sin(phi)+sin(5phi))
        //=> [0, 1/8, 0, 1/16, 0 , -1/16] - sinphi
        for (int i = 0; i < array_cos.length; i++) {
            for(int j = 0; j < array_sin_odd.length; j++) {
                result[i+j]+=0.5*array_cos[i]*array_sin_odd[j];//на месте суммы индексом массиво кос и син находим полусумму коэфф
                if(i==j) {
                    result[Math.abs(i - j)] = 0;//умножая чет из син (которые равны 0) на любое место в косинусе = 0; умножая нечет из кос (которые равны 0) на любое местов син = 0
                }else{
                    if(i<j) {
                        result[Math.abs(i - j)] += 0.5 * array_cos[i] * array_sin_odd[j];
                    }else{
                        result[Math.abs(i - j)] += -0.5 * array_cos[i] * array_sin_odd[j];
                    }
                }
//                System.out.println("i: " + i + "  j: " + j + " r " +result[4]);
//                if((j+i)==4 || Math.abs(i - j)==4) {
//                    System.out.println("i: " + i + "  j: " + j + " r " + 0.5 * array_cos[i] * array_sin_odd[j]);
//                }
            }
        }

        for (int i =0; i < result.length; i++) {
            System.out.println(result[i]);
        }

        return result;
    }

    public static double[] parse_member_from_equation(String s) {
        double[] res = null; //[const, x...x^n, y...y^n]
        int position_x = s.indexOf("x");
        int position_y = s.indexOf("y");
        if (position_y == -1 && position_x == -1) {
            res = new double[]{Double.parseDouble(s) , 0.0 , 0.0};
         //   return  res;
        }
        double power_of_x = 0;
        if (!(position_x == -1) && position_y == -1) {
            String temp_for_lenght = s.substring(0,position_x);
            if(temp_for_lenght.length()==1) {
                temp_for_lenght += "1";
            }
            if (position_x+1 < s.length() && s.toCharArray()[position_x+1]=='^') {
                power_of_x = Double.parseDouble(s.substring(position_x+2));
            }
            else {
                power_of_x = 1;
            }
            res = new double[]{Double.parseDouble(temp_for_lenght), power_of_x , 0.0};
         //   return  res;
        }

        double power_of_y = 0;
        if (position_x == -1 && !(position_y == -1)) {
            String koeff_before = s.substring(0,position_y);
            if(koeff_before.length()==1) {
                koeff_before += "1";
            }
            if (position_y+1 < s.length() && s.toCharArray()[position_y+1]=='^') {
                power_of_y = Double.parseDouble(s.substring(position_y+2));
            }
            else {
                power_of_y = 1;
            }
            res = new double[]{Double.parseDouble(koeff_before), 0.0 , power_of_y};
          //  return  res;
        }

        if (!(position_x == -1) && !(position_y == -1)) {
            String koeff_before = s.substring(0,position_x);
            if(koeff_before.length()==1) {
                koeff_before += "1";
            }
            if (position_x+2 < s.length() && s.toCharArray()[position_x+1]=='^') {//изменение 11.02 23:20 добавила position_x+2 < s.length() &&
                power_of_x = Double.parseDouble(s.substring(position_x+2,position_y));
            }
            else {
                power_of_x = 1;
            }
            if (position_y+2 < s.length() && s.toCharArray()[position_y+1]=='^') {//изменение 11.02 23:20 добавила position_y+2 < s.length() &&
                power_of_y = Double.parseDouble(s.substring(position_y+2));
            }
            else {
                power_of_y = 1;
            }
            res = new double[]{Double.parseDouble(koeff_before), power_of_x , power_of_y};
           // return  res;
        }

        return res;
    }

    public static int define_trig (double[] after_parse) {
        int option = 0; // const - 1, x^n n-even - 2, x^n n-odd - 3, y^m m-even - 4, y^m m-odd - 5, x^n*y^m m-even - 6, x^n*y^m m-odd - 7
        if (after_parse[1] == 0 && after_parse[2]==0) {
            option = 1;
        }
        else if (after_parse[2]==0) {
            if (after_parse[1]%2==0) {
                option = 2;
            }
            else {
                option = 3;
            }
        }
        else if (after_parse[1]==0) {
            if (after_parse[2]%2==0) {
                option = 4;
            }
            else {
                option = 5;
            }
        }
        else if (after_parse[1]!=0 && after_parse[2]!=0) {
            if (after_parse[2]%2==0) {
                option = 6;
            }
            else {
                option = 7;
            }
        }
        return option;
    }
    //изменение от 11.02 22:10 - добавила умножение на коэфф
    //изменение от 11.02 22:30 - добавила умножение на r^n
    public static double[][] polar_coord(double[] after_parse, int option, int array_len, int r) {
        double[][] triginomertic_funct = new double[2][array_len];
        double[] temp = null;

        switch(option){
            case 1:
                triginomertic_funct[0][0] = after_parse[0];
                triginomertic_funct[1][0] = after_parse[0];
                break;

            case 2:
                temp = koeff_cos_even((int)after_parse[1]);
                for(int i=0; i<temp.length;i++){
                    if (temp[i]!=0.0) {
                        triginomertic_funct[0][i] = temp[i] * after_parse[0] * Math.pow(r,(int)after_parse[1]);// * after_parse[0]
                    }
                }
                break;

            case 3:
                temp  = koeff_cos_odd((int)after_parse[1]);
                for(int i=0; i<temp.length;i++){
                    if (temp[i]!=0.0) {//этот if нужен так как иначе на первом месте получаем -0.0 если коэфф отриц
                        triginomertic_funct[0][i] = temp[i] * after_parse[0]* Math.pow(r,(int)after_parse[1]);// * after_parse[0]
                    }
                }
                break;

            case 4:
                temp = koeff_sin_even((int)after_parse[2]);
                for(int i=0; i<temp.length;i++){
                    if (temp[i]!=0.0) {
                        triginomertic_funct[0][i] = temp[i] * after_parse[0] * Math.pow(r,(int)after_parse[2]);// * after_parse[0]
                    }
                }
                break;

            case 5:
                temp = koeff_sin_odd((int)after_parse[2]);
                for(int i=0; i<temp.length;i++){
                    if (temp[i]!=0.0) {
                        triginomertic_funct[1][i] = temp[i] * after_parse[0] * Math.pow(r,(int)after_parse[2]);// * after_parse[0]
                    }
                }
                break;

            case 6:
                temp = mixed_multy(after_parse[0],(int)after_parse[1],(int)after_parse[2]);
                for(int i=0; i<temp.length;i++){
                    if(temp[i]!=0.0) {
                        triginomertic_funct[0][i] = temp[i] * after_parse[0] * Math.pow(r,(int)(after_parse[1]+after_parse[2]));// * after_parse[0]
                    }
                }
                break;

            case 7:
                temp= mixed_multy_sin(after_parse[0],(int)after_parse[1],(int)after_parse[2]);
                for(int i=0; i<temp.length;i++){
                    if(temp[i]!=0.0) {
                        triginomertic_funct[1][i] = temp[i] * after_parse[0] * Math.pow(r,(int)(after_parse[1]+after_parse[2]));//* after_parse[0]
                    }
                }
                break;
        }
        System.out.println("------------------");
        for(int i=0;i<2;i++){
            for(int j=0;j<triginomertic_funct[i].length;j++){
                System.out.print(triginomertic_funct[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("------------------\n");

        return triginomertic_funct;
    }

    static double[][] conditional_in_polar_coord(String conditional, int r) {
        String[] res = parse_to_array(conditional);
        double[] after_parse = null;
        double max_power = 0; //чтобы определить макс длину массива
        for (int i = 0; i < res.length; i++) {
            after_parse = parse_member_from_equation(res[i]);
            for (int j =0; j<after_parse.length; j++) {
                //System.out.print(after_parse[j] + " " );
                if (j==0 && after_parse[j+1]+after_parse[j+2] > max_power) {
                    max_power = after_parse[j+1]+after_parse[j+2];
                }
                //System.out.print("\nmax_power" + max_power + " " );
            }
            //System.out.println(define_trig(after_parse));
            //System.out.println(res[i]);
            //polar_coord(after_parse,define_trig(after_parse),(int) max_power);//вместо res.length -> max_power (изменение от 11.02 23:00)
        }
        double[][] summ_trig = new double[2][(int)max_power+1];
        double[][] trig_in_polar = null;
        // r_ 1 это только для положительных слагаемых, а должно еще быть r_1 ^ (-1*n)
        for (int i = 0; i < res.length; i++) {
            after_parse = parse_member_from_equation(res[i]);
            System.out.println(res[i]);
            trig_in_polar = polar_coord(after_parse,define_trig(after_parse),(int) max_power+1, r);//вместо res.length -> max_power (изменение от 11.02 23:00)
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < trig_in_polar[j].length; k++) {
                    summ_trig[j][k] += trig_in_polar[j][k];
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < summ_trig[i].length; j++) {
                System.out.print(summ_trig[i][j] + " ");
            }
            System.out.println();
        }
        return summ_trig;
    }

    static double[][] create_matrix(int r,double[][]koeff_cond, double[][] koeff_another) {
        int all_columns = koeff_cond[0].length;
        int all_rows = koeff_cond.length;

        double[][]matrix_first_for_gauss = new double[all_columns*2][all_columns*4];
        //int k = 0;
        for(int i = 0; i < all_columns; i++) {
            for (int j = 0; j < all_rows; j++) {
                if (koeff_cond[j][i]!=0 || koeff_another[j][i]!=0) {//проверка непустоты элемента не только в текущем наборе коэфф, но и во втором наборе
                    if (j == 0) {
                        if (i == 0) {
                            matrix_first_for_gauss[j][0] = 1;
                            matrix_first_for_gauss[j][1] = Math.log(r);
                        } else {
                            matrix_first_for_gauss[i][i * 2] = Math.pow(r, i);
                            matrix_first_for_gauss[i][i * 2 + 1] = Math.pow(r, -i);
                        }
                    }
                    if (j == 1) {
                        if (i == 0) {
//                            matrix_first_for_gauss[all_columns][all_columns * 2] = 1;
//                            matrix_first_for_gauss[all_columns][all_columns * 2 + 1] = Math.log(r);
                        } else {
                            matrix_first_for_gauss[i + all_columns][i * 2 + all_columns * 2] = Math.pow(r, i);
                            matrix_first_for_gauss[i + all_columns][i * 2 + 1 + all_columns * 2] = Math.pow(r, -i);
                        }
                    }
                }
            }
            //k++;
        }


        for(int i = 0; i< matrix_first_for_gauss.length; i++) {
            for(int j = 0; j < matrix_first_for_gauss[i].length; j++) {
                System.out.print(matrix_first_for_gauss[i][j]+"  ");
            }
            System.out.println();
        }
        return matrix_first_for_gauss;
    }

    static double[] matrix_for_non_singular(double[][] matrix, double[] matrix_vector){

        double[] need_column = new double[matrix.length];
        double[] need_rows = new double[matrix.length];
        double[][] non_singular = null;
        double[] non_singular_vector = null;
        int dim_ns = 0;
        int half_matrix_len = matrix.length/2;


        for(int i = 0; i< matrix.length; i++) {//ряд
            for(int j = 0; j < matrix[i].length; j++) {//колонка
               // if(matrix[i][j] !=0 &&(i!=half_matrix_len && j!=half_matrix_len && i!=half_matrix_len+1 && j!=half_matrix_len+1)){

                if(matrix[i][j] !=0){
                    need_rows[i] = 1;
                    need_column[j] = 1;
                }
            }
        }


        System.out.println("rows");
        for(int j = 0; j < need_rows.length; j++) {
            System.out.print(need_rows[j]+"  ");
            if(need_rows[j] == 1) {
                dim_ns++;
            }
        }
        System.out.println();
        System.out.println("columns");
        for(int j = 0; j < need_column.length; j++) {
            System.out.print(need_column[j]+"  ");
        }
        System.out.println();

        non_singular = new double[dim_ns][dim_ns];
        non_singular_vector = new double[dim_ns];

        int row_ns = 0;
        int column_ns = 0;
        for(int i = 0; i< matrix.length; i++) {//ряд
            column_ns = 0;
            if (need_rows[i] == 1){
                for (int j = 0; j < matrix[i].length; j++) {//колонка
                    if (need_column[j] == 1){
                        non_singular[row_ns][column_ns] = matrix[i][j];
                        column_ns++;
                    }
                }
                row_ns++;
            }
        }

        row_ns = 0;
        for(int i = 0; i < matrix_vector.length; i++) {
            if (need_rows[i] == 1) {
                non_singular_vector[row_ns] = matrix_vector[i];
                row_ns++;
            }
        }

        System.out.println("non_singular");

        for(int i = 0; i< non_singular.length; i++) {//ряд
            for(int j = 0; j < non_singular[i].length; j++) {//колонка
                System.out.print(non_singular[i][j]+"  ");
            }
            System.out.println();
        }

        System.out.println("non_singular vector");

        for(int i = 0; i< non_singular_vector.length; i++) {
                System.out.print(non_singular_vector[i]+"  ");
        }

        //ГАУСС
        System.out.println();
        double[] x = lsolve(non_singular, non_singular_vector);

        // print results
        for (int i = 0; i < non_singular.length; i++) {
            System.out.println(x[i]);
        }
        //вместо need rows - need columns
        column_ns = 0;
        System.out.println("NEED COLUMN: ");
        for(int j = 0; j < need_column.length; j++) {
            System.out.print(need_column[j]+"  ");
            if(need_column[j] == 1) {
                need_column[j] = x[column_ns];
                        column_ns++;
            }
        }
    System.out.println("NEED COLUMN: ");
        for (int i = 0; i < need_column.length; i++) {
            System.out.print(need_column[i]+"  ");
        }

        return need_column;
    }

    // Gaussian elimination with partial pivoting
    public static double[] lsolve(double[][] A, double[] b) {
        int n = b.length;

        for (int p = 0; p < n; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double   t    = b[p]; b[p] = b[max]; b[max] = t;

            // singular or nearly singular
            if (Math.abs(A[p][p]) <= 1e-10) {
                throw new ArithmeticException("Matrix is singular or nearly singular");
            }

            // pivot within A and b
            for (int i = p + 1; i < n; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < n; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // back substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }

    static String print_result (double[] array_of_const) {
        String final_str = "";
        int k = array_of_const.length/2;
        boolean first_element= false;
        boolean const_exist = false;
        for(int i = 0; i < array_of_const.length; i++) {
            if (array_of_const[i] != 0) {
                if (i==0 && const_exist == false) {
                    final_str += "(" + array_of_const[i] + ") +";
                    const_exist = true;
                }
                else if (const_exist == true) {
                    final_str += " (" + array_of_const[i] + ") ln(r) + ";
                    const_exist = false;
                }
                else {
                    if (i <= k && first_element == false) {
                        int step = i / 2;
                        final_str += "(" + array_of_const[i] + ") * r^(" + step + ") * cos (" + step + " * phi)";
                        first_element = true;
                    } else if (i <= k && first_element == true) {
                        int step = i / 2;
                        final_str += " + (" + array_of_const[i] + ") * r^(-" + step + ") * cos (" + step + " * phi) ";
                        first_element = false;
                    }
                    if (i > k && first_element == false) {
                        int step = i / 2 - k / 2;
                        final_str += " + (" + array_of_const[i] + ") * r^(" + step + ") * sin (" + step + " * phi) ";
                        first_element = true;
                    } else if (i > k && first_element == true) {
                        int step = i / 2 - k / 2;
                        final_str += " + (" + array_of_const[i] + ") * r^(-" + step + ") * sin (" + step + " * phi) ";
                        first_element = false;
                    }
                }
            }
        }

        return final_str;
    }

    static double[][] extent_for_same_size(double[][] target, int desired_size) {
        double[][] extent_target = new double[2][desired_size];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < desired_size; j++) {
                if (target[i].length > j)
                    extent_target[i][j] = target[i][j];
            }
        }
        return extent_target;
    }

        //TO_DO:
    //вроде правильно умножаю полярные коорд на коэфф перед ними и радиус кольца
    //надо их как то сложить
    //а потом отделить константы, косинусы разных аргументом, синусы разных аргументов
    //то есть пусть получилось: для const=[3,0,0], cosphi = [0.5,0,0.5], sinphi = [0,0,1], тогда
    // С_1 + С_2*ln(r) = const[0] + cosphi[0] = 3 + 0.5
    // r^2*C_3*cos(2*phi) + r^(-2)*C_4*cos(2*phi) = cosphi[2] = 0.5
    // r^2*C_5*sin(2*phi) + r^(-2)*C_6*sin(2*phi) = sinphi[2] = 1
    //
    //ВЫНЕСТИ В ОТДЕЛЬНУЮ ФУНКЦИЮ СЛОЖЕНИЕ
    //
    //ВЫВОДИТЬ КОНЕЧНЫЙ РЕЗУЛЬТАТ (цешки уже есть)
    public static void main(String[] args) {


        //String s = "1-x+1x^4+1y^5-xy^3-y^4+x^3y^2";
        //String s = "4y^2-2x+x^4-xy^2-8x^5y";
        String conditional_first = "3+x^2";
        String conditional_second = "xy-y+1";
       // String conditional_second = "0.03125x^2+0.125";
        int r_1 = 1;
        int r_2 = 2;
        double[][]koeff_cond_first_in_polar = conditional_in_polar_coord(conditional_first, r_1);//коэфф в разложении граничной функции при соотв радиусе
        double[][]koeff_cond_second_in_polar = conditional_in_polar_coord(conditional_second, r_2);//коэфф в разложении граничной функции при соотв радиусе
        if (koeff_cond_first_in_polar[0].length > koeff_cond_second_in_polar[0].length) {
            koeff_cond_second_in_polar = extent_for_same_size(koeff_cond_second_in_polar, koeff_cond_first_in_polar[0].length);
        }
        else if (koeff_cond_second_in_polar[0].length > koeff_cond_first_in_polar[0].length) {
            koeff_cond_first_in_polar = extent_for_same_size(koeff_cond_first_in_polar, koeff_cond_second_in_polar[0].length);
        }

        double[][] matrix_for_first_cond = create_matrix(r_1, koeff_cond_first_in_polar, koeff_cond_second_in_polar);//матрица из произв констант соотв радиусу внешнего круга
        double[][] matrix_for_second_cond = create_matrix(r_2, koeff_cond_second_in_polar, koeff_cond_first_in_polar);
        System.out.println("koeff_cond_first_in_polar");
        for(int i = 0; i< koeff_cond_first_in_polar.length; i++) {
            for(int j = 0; j < koeff_cond_first_in_polar[i].length; j++) {
                System.out.print(koeff_cond_first_in_polar[i][j]+"  ");
            }
            System.out.println();
        }        System.out.println("koeff_cond_second_in_polar");
        for(int i = 0; i< koeff_cond_second_in_polar.length; i++) {
            for(int j = 0; j < koeff_cond_second_in_polar[i].length; j++) {
                System.out.print(koeff_cond_second_in_polar[i][j]+"  ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("matrix_for_first_cond");
        for(int i = 0; i< matrix_for_first_cond.length; i++) {
            for(int j = 0; j < matrix_for_first_cond[i].length; j++) {
                System.out.print(matrix_for_first_cond[i][j]+"  ");
            }
            System.out.println();
        }        System.out.println("matrix_for_second_cond");
        for(int i = 0; i< matrix_for_second_cond.length; i++) {
            for(int j = 0; j < matrix_for_second_cond[i].length; j++) {
                System.out.print(matrix_for_second_cond[i][j]+"  ");
            }
            System.out.println();
        }
        System.out.println();


        System.out.println("DOUBLE MATRIX: ");

        double[][] final_matrix = new double[matrix_for_first_cond.length*2][matrix_for_first_cond[0].length];

        for(int i = 0; i < matrix_for_first_cond.length; i++) {
            for (int j = 0; j < matrix_for_first_cond[i].length; j++) {
                final_matrix[i][j]=matrix_for_first_cond[i][j];
                System.out.print(final_matrix[i][j]+"  ");
            }
            System.out.println();
        }

        for(int i = 0; i < matrix_for_second_cond.length; i++) {
            for (int j = 0; j < matrix_for_second_cond[i].length; j++) {
                final_matrix[i+matrix_for_first_cond.length][j]=matrix_for_second_cond[i][j];
                System.out.print(final_matrix[i+matrix_for_first_cond.length][j]+"  ");
            }
            System.out.println();
        }

        System.out.println();
        double[] res_for_matrix = new double [koeff_cond_first_in_polar.length*koeff_cond_first_in_polar[0].length*2];

        int counter = 0;
        for(int i = 0; i < koeff_cond_first_in_polar.length; i++) {
            for (int j = 0; j < koeff_cond_first_in_polar[i].length; j++) {
                res_for_matrix[counter]=koeff_cond_first_in_polar[i][j];
                System.out.print(res_for_matrix[counter]+"  ");
                counter++;
            }
        }

        for(int i = 0; i < koeff_cond_second_in_polar.length; i++) {
            for (int j = 0; j < koeff_cond_second_in_polar[i].length; j++) {
                res_for_matrix[counter]=koeff_cond_second_in_polar[i][j];
                System.out.print(res_for_matrix[counter]+"  ");
                counter++;
            }
        }
    System.out.println("\nRES_FOR_MATRIX");
        for(int i = 0; i < res_for_matrix.length; i++) {
            System.out.print(res_for_matrix[i]+"  ");
        }

        System.out.println("ДЛИНА " + final_matrix.length + "   " + final_matrix[0].length);

        double[] array_of_const = matrix_for_non_singular(final_matrix,res_for_matrix);

        System.out.println("\n"+print_result(array_of_const));

//        for(int i = 0; i < res.length; i++) {
//            for (int j = after_parse.length-1; j > -1; j--) {
//                if (after_parse[j]==0.0) { //то есть не существует степени при у, остается либо х либо константа
//                    if (after_parse[j-1] == 0.0) {//то есть не существует степени при х
//
//                    }
//                }
//            }
//        }

//        mixed_multy_sin(3,2,4);

//        String s = "x";
//        int max_power = parse(s);//узнаем максимальную степень, чтобы установить размер массива
//    // String s = "-y^3";
//        System.out.println("Массив коэфф при степенях в декартовой системе коорд:\n");
//        double[][] t = getArrayKoeff(s,max_power);
//        for(int i=0; i< t[0].length; i++) {
//            System.out.println(t[0][i]);
//        }

//        System.out.println("\nmax_power: "+max_power+"\n");
//
//        //считаем косинусы (пока только четных степеней)
//        double[] cos_even = new double[max_power+1];
//
//        for(int i = 1; i < max_power+1; i++) {
//            if (i%2==0) {
//                cos_even = koeff_cos_even(i);
//                for (int j = 0; j < cos_even.length; j++) {
//                    System.out.println(cos_even[j]);
//                }
//            }
//        }
//
//        System.out.println("\n");
//
//        double[] polar_coord_cos = new double[cos_even.length];
//        polar_coord_cos[0] = t[0][0] + cos_even[0];
//        System.out.println(polar_coord_cos[0]);
//        for (int i = 1; i < cos_even.length; i++) {
//            polar_coord_cos[i] = t[0][i] * cos_even[i];
//            System.out.println(polar_coord_cos[i]);
//        }
//
//        System.out.println("\n");
//        // считаем синусы (пока только нечетных степеней)
//
//        double[] sin_odd = new double[max_power+1];
//        sin_odd[0] = 0;
//        // что то не так со счетчиком i, если начинать с 0 или 1 то появляются лишние две строки
//        for(int i = 3; i < max_power+1; i++) {
//            if (i%2!=0) {
//                sin_odd = koeff_sin_odd(i);
//                for (int j = 0; j < sin_odd.length; j++) {
//                    System.out.println(sin_odd[j]);
//                }
//            }
//        }
//
//        System.out.println("\n");
//
//        double[] polar_coord_sin = new double[sin_odd.length+1];
//        polar_coord_sin[0] = polar_coord_cos[0];
//        System.out.println(polar_coord_sin[0]);//нулевой элемент не = -2 а = точу же что и косинус
//        for (int i = 1; i < cos_even.length+1; i++) {
//            if (t[1][i]==0.0) {
//                polar_coord_sin[i] =sin_odd[i];
//            }
//            else {
//                polar_coord_sin[i] = t[1][i] * sin_odd[i];
//            }
//            System.out.println(polar_coord_sin[i]);
//        }

//        double[] sin_odd =  koeff_sin_odd(5);
//        for (int i = 0; i < sin_odd.length; i++) {
//            System.out.println(sin_odd[i]);
//        }

//        System.out.println("\n");
//        double[] cos_even =  koeff_cos_odd(1);
//        for (int i = 0; i < cos_even.length; i++) {
//            System.out.println(cos_even[i]);
//        }

//        double[] sin_even =  koeff_sin_even(6);
//        for (int i = 0; i < sin_even.length; i++) {
//            System.out.println(sin_even[i]);
//        }



//        System.out.println(koeff_even(4));

//        Scanner in = new Scanner(System.in);
//        double r_1;
//        double r_2;
//        double x_0;
//        double y_0;
//
//        System.out.println("Введите радиус внутренней окружности: ");
//        r_1 = in.nextDouble();
//        System.out.println("Введите радиус внешней окружности: " );
//        r_2 = in.nextDouble();
//        System.out.println("Введите центр симметрии: ");
//        x_0 = in.nextDouble();
//        y_0 = in.nextDouble();
//        in.close();
//
//        if (r_1 < 0) {
//            System.out.println("Радиус внутренней окружности не может быть отрицательныйм");
//        }
//        if (r_2 < 0) {
//            System.out.println("Радиус внешней окружности не может быть отрицательныйм");
//        }
//        if (r_1 > r_2) {
//            System.out.println("Радиус внутренней окружности не может быть больше радиуса внешней окружности");
//        }
//        добавить проверку что введено именно число для всех параметров
//        if (x_0 < 0) || (x_0 == Double.parseDouble(x_0))
    }
}