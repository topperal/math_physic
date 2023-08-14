import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parsing {

    public static long factorialUsingRecursion(int n) { //рекурсивная функция факториала, используется для Сочетания из n по k
        if (n==0 || n==1) {
            return 1;
        }
        return n * factorialUsingRecursion(n - 1);
    }

//    public static double koeff_even (int n) {
//        return factorialUsingRecursion (n)/(factorialUsingRecursion(n/2)*factorialUsingRecursion(n-n/2))/Math.pow(2,n);
//    }

    public static String dividing(String s) {
        String new_s = "";
        String res_str = "";
        int prevoius_end = 0;
        boolean power1 = true;
        Pattern p = Pattern.compile("\\/\\(x\\^2\\+y\\^2\\)(\\^(-|)\\d*\\.\\d*|-|\\+|$)");
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start=m.start();
            int end=m.end();
            System.out.println("Найдено совпадение " + s.substring(start,end) + " с "+ start + " по " + (end-1) + " позицию");
            new_s = m.group();
            System.out.println(new_s);
            if(new_s.length() == 10 || new_s.length() == 11){
                new_s = "1.0";
                power1 = true;
            }else{
                power1 = false;
                if(new_s.charAt(new_s.length()-1)=='+' || new_s.charAt(new_s.length()-1)=='-'){
                    new_s = new_s.substring(11,new_s.length()-1);
                }else{
                    new_s = new_s.substring(11,new_s.length());
                }

            }
            String t = s.substring(prevoius_end,start);
            res_str += s.substring(prevoius_end,start)+"R"+new_s.replace('-','N')+"r";
            if(power1) {
                prevoius_end = --end;
            }else{
                prevoius_end = end;
            }
        }
        res_str += s.substring(prevoius_end,s.length());
        // new_s = s.replaceAll("\\/\\(x\\^2\\+y\\^2\\)(.*?)($|\\+|-)", "R");
        return res_str;
    }
    public static String[] parse_to_array(String s, double r){//парсим строку из "x^2-5y+3" в массив: [+x^2, -5y, +3]
        s = dividing(s);
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
        String[] simplify_eq =  without_ln(final_string, r);
        return simplify_eq;
    }

    public static String[] without_ln (String[] parsed_string_by_sign, double r) {
        String[] string_wihtout_ln = null;
        double all_const = 0;
        ArrayList<String> desired_elements = new ArrayList<>();
        for (int i = 0; i < parsed_string_by_sign.length; i++) {
            if (parsed_string_by_sign[i].indexOf("x") == -1 && parsed_string_by_sign[i].indexOf("y") == -1) {
                String sign = parsed_string_by_sign[i].substring(0,1);
                double division = 1;
                if (parsed_string_by_sign[i].indexOf("ln") != -1) {
                    int pos_ln = parsed_string_by_sign[i].indexOf("ln");
                    int start_div = parsed_string_by_sign[i].length();
                    double koeff_before_ln = Double.parseDouble(sign+"1");
                    if (pos_ln > 1) {
                        koeff_before_ln = Double.parseDouble(parsed_string_by_sign[i].substring(1, pos_ln));
                    }
                    if (parsed_string_by_sign[i].indexOf("R") != -1) {
                        start_div = parsed_string_by_sign[i].indexOf("R");
                        int end_div = parsed_string_by_sign[i].indexOf("r");
                        int sign_div = 1;
                        int negative = 0;
                        if (parsed_string_by_sign[i].indexOf("N") != -1) {
                            sign_div = -1;
                            negative = 1;
                        }
                        division = sign_div * Double.parseDouble(parsed_string_by_sign[i].substring(start_div+1+negative, end_div));
                        double arg_ln = Double.parseDouble(parsed_string_by_sign[i].substring(pos_ln+2, start_div));
                        double arg = koeff_before_ln * Math.log(arg_ln)/Math.pow(r,division);
                        all_const += arg;
                    }
                    else{
                        double arg_ln = Double.parseDouble(parsed_string_by_sign[i].substring(pos_ln+2));
                        double arg = koeff_before_ln* Math.log(arg_ln);
                        all_const += arg;
                    }
                }
                else {
                    int start_div = 1;
                    double koeff_before_const = Double.parseDouble(sign+"1");
                    double origin_const = 0;
                    if (parsed_string_by_sign[i].indexOf("R") != -1) {
                        start_div = parsed_string_by_sign[i].indexOf("R");
                        int end_div = parsed_string_by_sign[i].indexOf("r");
                        int sign_div = 1;
                        if (parsed_string_by_sign[i].indexOf("N") != -1) {
                            sign_div = -1;
                            start_div++;
                        }
                        division = sign_div * Double.parseDouble(parsed_string_by_sign[i].substring(start_div+1, end_div));
                        origin_const = Double.parseDouble(parsed_string_by_sign[i].substring(1,start_div-1));
                        all_const += koeff_before_const * origin_const/Math.pow(r,division);
                    }
                    else {
                        origin_const = Double.parseDouble(parsed_string_by_sign[i].substring(start_div));
                        all_const += koeff_before_const * origin_const;
                    }

                }
            }
            else {
                desired_elements.add(parsed_string_by_sign[i]);
            }
        }
        String signed_all_const = String.valueOf(all_const);
        if (signed_all_const.charAt(0)!='-') {
            signed_all_const = "+" + signed_all_const;
        }
        desired_elements.add(signed_all_const);
        string_wihtout_ln = desired_elements.toArray(String[]::new);
        return string_wihtout_ln;
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

    public static double[] parentheses_for_cos(int n, double x_0, double r) {
        double[] result = new double [n+1];
        //int r = 3;
        for (int i = result.length-1; i >= 0; i--) {
            result[i] = (factorialUsingRecursion(n)/(factorialUsingRecursion(i)*factorialUsingRecursion(n-i)))*Math.pow(x_0, n-i);
        }
        double[]temp = new double[result.length];
        double[]summ_all_cos = new double[result.length];
        for(int i = 0; i < result.length; i++) {//заполняем первые два элемента результирующего массива первыми двумя элементами исх если они существуют
            summ_all_cos[i] = result[i];
            if (i == 1) {
                summ_all_cos[i] = r * result[i];
                break;
            }
        }
        double koeff = 0;

        for (int i = result.length-1; i > 1; i--) {
            if (result[i]!=0) {
                koeff = Math.pow(r,n) * result[i];
                if(i%2==0) {
                    temp = koeff_cos_even(i);
                }
                else {
                    temp = koeff_cos_odd(i);
                }
                for(int k = 0; k < temp.length; k++) {
                    summ_all_cos[k] += temp[k] * koeff;
                }
            }
        }
        return summ_all_cos;
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

    public static double[][] parentheses_for_sin(int n, double y_0, double r) {
        double[] result = new double [n+1];
        for (int i = result.length-1; i >= 0; i--) {
            result[i] = Math.pow(r,i) *(factorialUsingRecursion(n)/(factorialUsingRecursion(i)*factorialUsingRecursion(n-i)))*Math.pow(y_0, n-i);
        }
        double[]temp = new double[result.length];
        double[][]summ_all_sin = new double[2][result.length];
        for(int i = 0; i < result.length; i++) {//заполняем первые два элемента результирующего массива первыми двумя элементами исх если они существуют
            if(i==0) {
                summ_all_sin[0][i] = result[i];
            }
            if (i == 1) {
                summ_all_sin[1][i] = result[i];// Math.pow(r,n) *
                break;
            }
        }
        double koeff = 0;
        for (int i = result.length-1; i > 1; i--) {//LAST UPDATE
            if (result[i]!=0) {
                koeff = result[i];//Math.pow(r,n)*
                if(i%2==0) {
                    temp = koeff_sin_even(i);
                    for(int k = 0; k < temp.length; k++) {
                        summ_all_sin[0][k] += temp[k] * koeff;
                    }
                }
                if(i%2!=0) {
                    temp = koeff_sin_odd(i);
                    for(int k = 0; k < temp.length; k++) {
                        summ_all_sin[1][k] += temp[k] * koeff;
                    }
                }
            }
        }
        //summ_all_sin[1][0] = summ_all_sin[0][0];
        return summ_all_sin;
    }

    public static double[] mixed_multy (double koeff,int n, int m, double x_0, double y_0, double r) {//возвращаем коэфф при смешанном произведении в разложении которого только косинусы (случай когда при x/cos любая степень, а при y/sin четная)
        double[] result = new double[n+m+1];//xy^2 -> cos(phi)*sin(phi)^2 -> [0, 0.25, 0, -0.25], размерность = 1(степень при cos)+2(степень при sin)+1
        double[] array_cos = null;//массив с коэфф разложения косинуса
        array_cos = parentheses_for_cos(n, x_0, r);

//        for (int i =0; i < array_cos.length; i++) {
//            System.out.println(array_cos[i]);
//        }
//
//        System.out.println("\nSin"); UPDATE
        //double[] array_sin_even = koeff_sin_even(m);//массив с коэфф разложения синуса (так как его степень четная, то он раскладывается по косинусам)
        double[][] array_sin = parentheses_for_sin(m, y_0, r);
//        System.out.println("SIN FROM ARRAY SIN");
//        for (int i = 0; i < array_sin[0].length; i++) {
//            System.out.print(array_sin[1][i] + "   ");
//        }
        double[] array_sin_even = array_sin[0];
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
        //double mult = 0;
        for (int i = 0; i < array_cos.length; i++) {//проходим по массиву коэфф от х
            for(int j = 0; j < array_sin_even.length; j++) {//проходим по массиву коэфф от у
                //if (i==0) {
                //   mult = Math.pow(r,m);
                //}
                //else {
                //    mult = Math.pow(1,m);
                //}
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

    public static double[] mixed_multy_sin (double koeff,int n, int m, double x_0, double y_0, double r) {//получаем коэфф смешанного произв в разложении которого всегда синусы (т.е. когда степень х любая, а степень у - нечетная)
        double[] result = new double[n+m+1];//cos(phi) * sin(phi) -> [0,0,0.5] -> 0.5*sin(2phi)
        double[] array_cos = null;//массив с коэфф разложения косинуса
        array_cos = parentheses_for_cos(n, x_0, r);

//        for (int i =0; i < array_cos.length; i++) {
//            System.out.println(array_cos[i]);
//        }

        // System.out.println("\nSin");
        double[][] array_sin = parentheses_for_sin(m, y_0, r);
        double[] array_sin_odd = array_sin[1];//разложение у по нечетной степени, что всегда дает разложение по синусам

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
//        for (int i = 1; i < array_cos.length; i++) {
//            for(int j = 0; j < array_sin_odd.length; j++) {
//                result[i+j]+=0.5*array_cos[i]*array_sin_odd[j];//на месте суммы индексом массиво кос и син находим полусумму коэфф
//                if(i==j) {
//                    result[Math.abs(i - j)] = 0;//умножая чет из син (которые равны 0) на любое место в косинусе = 0; умножая нечет из кос (которые равны 0) на любое местов син = 0
//                }else{
//                    if(i<j) {
//                        result[Math.abs(i - j)] += 0.5 * array_cos[i] * array_sin_odd[j];
//                    }else{
//                        result[Math.abs(i - j)] += -0.5 * array_cos[i] * array_sin_odd[j];
//                    }
//                }
////                System.out.println("i: " + i + "  j: " + j + " r " +result[4]);
////                if((j+i)==4 || Math.abs(i - j)==4) {
////                    System.out.println("i: " + i + "  j: " + j + " r " + 0.5 * array_cos[i] * array_sin_odd[j]);
////                }
//            }
//        }

//        array_cos = new double[]{0.0,1.0,0.0,0.0,0.0,0.0};
//        array_sin_odd = new double[]{0.0,1.0,0.0,0.0,0.0,0.0};
//        result = new double[11];
        // получение константы
        for(int j = 0; j < array_sin_odd.length; j++) {
            result[j] = array_cos[0] * array_sin_odd[j];//Math.pow(r,m) *
        }
        for (int i = 1; i < array_cos.length; i++) {
            for(int j = 0; j < array_sin_odd.length; j++) {
                double mult = 0.5 * array_cos[i]*array_sin_odd[j];//* Math.pow(r,m)
                double koeff1 = 1.0;
                if(j<i){
                    koeff1 = -1.0;
                } else if (j==i) {
                    koeff1 = 0.0;
                }
                if(Math.abs(j-i) != 0) {
                    result[Math.abs(j - i)] +=  koeff1 * mult;
                }
                result[j + i] += mult;

            }
        }

        System.out.println("mixed_multy_sin");
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
            int position_ln = s.indexOf("ln");
            if (position_ln != -1) {
                position_ln += 2;
                //s.substring(position_ln+2);
            }
            else {
                res = new double[]{Double.parseDouble(s), 0.0, 0.0, 0.0};
            }

            //   return  res;
        }
        double power_of_x = 0;
        if (!(position_x == -1) && position_y == -1) {
            String temp_for_lenght = s.substring(0,position_x);
            if(temp_for_lenght.length()==1) {
                temp_for_lenght += "1";
            }
            if (position_x+1 < s.length() && s.toCharArray()[position_x+1]=='^') {
                if (s.indexOf("R") == -1) {
                    power_of_x = Double.parseDouble(s.substring(position_x+2));
                }
                else {
                    power_of_x = Double.parseDouble(s.substring(position_x + 2, s.indexOf("R")));
                }
            }
            else {
                power_of_x = 1;
            }
            res = new double[]{Double.parseDouble(temp_for_lenght), power_of_x , 0.0, 0.0};
            //   return  res;
        }

        double power_of_y = 0;
        if (position_x == -1 && !(position_y == -1)) {
            String koeff_before = s.substring(0,position_y);
            if(koeff_before.length()==1) {
                koeff_before += "1";
            }
            if (position_y+1 < s.length() && s.toCharArray()[position_y+1]=='^') {
                if (s.indexOf("R") == -1) {
                    power_of_y = Double.parseDouble(s.substring(position_y + 2));
                }
                else {
                    power_of_y = Double.parseDouble(s.substring(position_y + 2, s.indexOf("R")));
                }
            }
            else {
                power_of_y = 1;
            }
            res = new double[]{Double.parseDouble(koeff_before), 0.0 , power_of_y, 0.0};
            //  return  res;
        }

        if (!(position_x == -1) && !(position_y == -1)) {
            String koeff_before = "";

            if (position_y < position_x) {
                koeff_before = s.substring(0,position_y);
            }
            else {
                koeff_before = s.substring(0,position_x);
            }
            if(koeff_before.length()==1) {
                koeff_before += "1";
            }
            if (position_x+2 < s.length() && s.toCharArray()[position_x+1]=='^') {//изменение 11.02 23:20 добавила position_x+2 < s.length() &&
                if (s.indexOf("R") == -1) {
                    power_of_x = Double.parseDouble(s.substring(position_x+2,position_y));
                }
                else {
                    power_of_x = Double.parseDouble(s.substring(position_x + 2, s.indexOf("R")));
                }
            }
            else {
                power_of_x = 1;
            }
            if (position_y+2 < s.length() && s.toCharArray()[position_y+1]=='^') {//изменение 11.02 23:20 добавила position_y+2 < s.length() &&
                if (s.indexOf("R") == -1) {
                    power_of_y = Double.parseDouble(s.substring(position_y+2));
                }
                else {
                    power_of_y = Double.parseDouble(s.substring(position_y + 2, s.indexOf("R")));
                }
            }
            else {
                power_of_y = 1;
            }
            res = new double[]{Double.parseDouble(koeff_before), power_of_x , power_of_y, 0.0};
            // return  res;
        }

        if (s.indexOf("R") != -1 ) {
            int end_divider_power = s.indexOf("r");
            if (s.indexOf("N") != -1) {
                res[3] = (-1)*Double.parseDouble(s.substring(s.indexOf("N")+1, end_divider_power));
            }
            else {
                res[3] = Double.parseDouble(s.substring(s.indexOf("R")+1, end_divider_power));
            }
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
    public static double[][] polar_coord(double[] after_parse, int option, int array_len, double r, double x_0, double y_0) {
        double[][] triginomertic_funct = new double[2][array_len];
        double[] temp = null;
        double[] temp_cos = null;
        double[] temp_sin = null;
        double division = 1;
        if (after_parse[3] == 0.0) {
            division = 1.0;
        }
        else {
            division = Math.pow(r,after_parse[3]);
        }

        switch(option){
            case 1:
                triginomertic_funct[0][0] = after_parse[0];
                triginomertic_funct[1][0] = after_parse[0];
                break;

            case 2:
//                double division = 0;
//                if (after_parse[3] !=1) {
//                    division = after_parse[1]-after_parse[3];
//                }
                temp = parentheses_for_cos((int)after_parse[1], x_0, r);

                for(int i=0; i<temp.length;i++){
                    if (temp[i]!=0.0) {//этот if нужен так как иначе на первом месте получаем -0.0 если коэфф отриц
                        //if(i==0) {//для констант
//                            if(after_parse[2]%2 == 0) {// четная степень
//                                int n = (int)after_parse[2];
//                                triginomertic_funct[0][i] = (x_0 +factorialUsingRecursion(n)/(factorialUsingRecursion(n/2)*factorialUsingRecursion(n-n/2)) / Math.pow(2, n)* Math.pow(r, n)) * temp[i];
//                            }else {
//                                triginomertic_funct[0][i] = x_0 * temp[i];
//                            }
                        //}else {
                        triginomertic_funct[0][i] = temp[i] * after_parse[0]/division;//  * Math.pow(r,(int)after_parse[1]*i)    * Math.pow(r, (int)after_parse[1]*i)
                        // }
                    }
                }

//                double const_from_cos_and_sin = triginomertic_funct[0][0] + triginomertic_funct[1][0];
//                triginomertic_funct[0][0] = const_from_cos_and_sin;
//                triginomertic_funct[1][0] = const_from_cos_and_sin;
                triginomertic_funct[1][0] = triginomertic_funct[0][0];
                break;

            case 3:
                temp  = parentheses_for_cos((int)after_parse[1], x_0, r);
                for(int i=0; i<temp.length;i++){
                    if (temp[i]!=0.0) {//этот if нужен так как иначе на первом месте получаем -0.0 если коэфф отриц
                        triginomertic_funct[0][i] = temp[i] * after_parse[0]/division;// * after_parse[0]* Math.pow(r,(int)after_parse[1]*i)  * Math.pow(r, (int)after_parse[1]*i)
                    }
                }
//                const_from_cos_and_sin = triginomertic_funct[0][0] + triginomertic_funct[1][0];
//                triginomertic_funct[0][0] = const_from_cos_and_sin;
//                triginomertic_funct[1][0] = const_from_cos_and_sin;
                triginomertic_funct[1][0] = triginomertic_funct[0][0];
                break;

            case 4:
                temp = koeff_sin_even((int)after_parse[2]);
//                for(int i=0; i<temp.length;i++){
//                    if (temp[i]!=0.0) {
//                        triginomertic_funct[0][i] = temp[i] * after_parse[0] * Math.pow(r,(int)after_parse[2]);// * after_parse[0]
//                    }
//                }
                triginomertic_funct = parentheses_for_sin((int)after_parse[2],y_0,r);
                for (int i = 0; i < triginomertic_funct.length; i++) {
                    for (int j = 0; j < triginomertic_funct[i].length; j++) {
                        if (temp[i]!=0.0 || triginomertic_funct[i][j] !=0) {
                            triginomertic_funct[i][j] *= after_parse[0]/division;// * Math.pow(r, (int) after_parse[2]*j)   * Math.pow(r, (int) after_parse[2])
                        }
                    }
                }

//                const_from_cos_and_sin = triginomertic_funct[0][0] + triginomertic_funct[1][0];
//                triginomertic_funct[0][0] = const_from_cos_and_sin;
//                triginomertic_funct[1][0] = const_from_cos_and_sin;

                triginomertic_funct[1][0] = triginomertic_funct[0][0];

                break;

            case 5:
                temp = koeff_sin_odd((int)after_parse[2]);
//                for(int i=0; i<temp.length;i++){
//                    if (temp[i]!=0.0) {
//                        triginomertic_funct[1][i] = temp[i] * after_parse[0] * Math.pow(r,(int)after_parse[2]);// * after_parse[0]
//                    }
//                }
                triginomertic_funct = parentheses_for_sin((int)after_parse[2],y_0, r);

                for (int i = 0; i < triginomertic_funct.length; i++) {
                    for (int j = 0; j < triginomertic_funct[i].length; j++) {
                        if (temp[i]!=0.0 || triginomertic_funct[i][j] !=0.0) {
                            triginomertic_funct[i][j] *= after_parse[0]/division;//UPDATE * Math.pow(r, (int) after_parse[2]*j)  * Math.pow(r, (int) after_parse[2])
                        }
                    }
                }

//                const_from_cos_and_sin = triginomertic_funct[0][0] + triginomertic_funct[1][0];
//                triginomertic_funct[0][0] = const_from_cos_and_sin;
//                if ((int)after_parse[2] > 1) {
//                    triginomertic_funct[1][0] = after_parse[0];
//                }

                triginomertic_funct[1][0] = triginomertic_funct[0][0];
                break;

            case 6:
                temp_cos = mixed_multy(after_parse[0],(int)after_parse[1],(int)after_parse[2], x_0, y_0, r);
                for(int i=0; i<temp_cos.length;i++){
                    if(temp_cos[i]!=0.0) {
                        triginomertic_funct[0][i] = temp_cos[i] * after_parse[0]/division;// * after_parse[0] * Math.pow(r,(int)((after_parse[1])*i))    * Math.pow(r,(int)((after_parse[1]+after_parse[2])))
                    }
                }
                temp_sin = mixed_multy_sin(after_parse[0],(int)after_parse[1],(int)after_parse[2], x_0, y_0, r);
                for(int i=0; i<temp_sin.length;i++){
                    if(temp_sin[i]!=0.0) {
                        triginomertic_funct[1][i] = temp_sin[i] * after_parse[0]/division;// * after_parse[0] * Math.pow(r,(int)((after_parse[2])*i))    * Math.pow(r,(int)((after_parse[2]+after_parse[2])))
                    }
                }

//                const_from_cos_and_sin = triginomertic_funct[0][0] + triginomertic_funct[1][0];
//                triginomertic_funct[0][0] = const_from_cos_and_sin;
//                triginomertic_funct[1][0] = const_from_cos_and_sin;

                triginomertic_funct[1][0] = triginomertic_funct[0][0];
                break;
//08.03.2022
            case 7:
                //int k = 0;
                temp_sin = mixed_multy_sin(after_parse[0],(int)after_parse[1],(int)after_parse[2], x_0, y_0, r);
                for(int i=0; i<temp_sin.length;i++){
                    if(temp_sin[i]!=0.0) {
                        triginomertic_funct[1][i] = temp_sin[i] * after_parse[0]/division;//* after_parse[0] * Math.pow(r,(int)((after_parse[2])*i))      * Math.pow(r,(int)(after_parse[2]))
                        //k++;
                    }
                }
                // k = 0;
                temp_cos = mixed_multy(after_parse[0],(int)after_parse[1],(int)after_parse[2], x_0, y_0, r);
                for(int i=0; i<temp_cos.length;i++){
                    if(temp_cos[i]!=0.0) {
                        triginomertic_funct[0][i] = temp_cos[i] * after_parse[0]/division;//* after_parse[0]* Math.pow(r,(int)((after_parse[1])*i))    * Math.pow(r,(int)((after_parse[1])))
                        //k++;
                    }
                }

//                const_from_cos_and_sin = triginomertic_funct[0][0] + triginomertic_funct[1][0];
//                triginomertic_funct[0][0] = const_from_cos_and_sin;
//                triginomertic_funct[1][0] = const_from_cos_and_sin;

                triginomertic_funct[1][0] = triginomertic_funct[0][0];
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

    static double[][] conditional_in_polar_coord(String conditional, double r, double x_0, double y_0) {
        String[] res = parse_to_array(conditional, r);
        double[] after_parse = null;
        double max_power = 0; //чтобы определить макс длину массива
        for (int i = 0; i < res.length; i++) {
            after_parse = parse_member_from_equation(res[i]);
            for (int j =0; j<after_parse.length-1; j++) {//UPDATE 10.03 -1 так как добавилось after_parse[3] которое отвечает за деление
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
        //System.out.println("conditional_in_polar_coord ");
        for (int i = 0; i < res.length; i++) {
            after_parse = parse_member_from_equation(res[i]);
            System.out.println(res[i]);
            trig_in_polar = polar_coord(after_parse,define_trig(after_parse),(int) max_power+1, r, x_0,y_0);//вместо res.length -> max_power (изменение от 11.02 23:00)
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < trig_in_polar[j].length; k++) {
                    summ_trig[j][k] += trig_in_polar[j][k];
                }
            }
        }
        if (summ_trig[1][0] != summ_trig[0][0]) {
            double constants = summ_trig[0][0] + summ_trig[1][0];
            summ_trig[1][0] = constants;
            summ_trig[0][0] = constants;
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < summ_trig[i].length; j++) {
                System.out.print(summ_trig[i][j] + " ");
            }
            System.out.println();
        }
        return summ_trig;
    }
}
