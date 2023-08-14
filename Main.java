import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.Math;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

public class Main {

    static public String u_r = "";
    static  public int max_cos = 0;
    static  public int max_sin = 0;
    public static String print_u_r(double[][] koeff_cond) {
        u_r = "";
        boolean first = false;
        for(int i = 0; i < koeff_cond.length; i++) {
            for(int j = 0; j < koeff_cond[i].length; j++) {
                if (koeff_cond[i][j] != 0.0) {
                    if (first == false) {
                        if (j == 0) {
                            u_r += "(" + koeff_cond[i][j]+ ") * r^(" + 2 * koeff_cond[0].length + ")";
                            first = true;
                            break;
                        }
//                        else if (j == 1) {
//                            u_r += "(" + koeff_cond[i][j] +") * r^(" + j + ")";
//                        }
                        else {
                            if (i==0) {
                                u_r += " (" + (1) * koeff_cond[i][j] + ") * r^(" + max_cos + ")";
                            }
                            else {
                                u_r += " (" + (1) * koeff_cond[i][j] + ") * r^(" + max_sin + ")";
                            }
                        }
                    }
                    if (i==0 && j !=0 && j == max_cos) {
                        u_r += " * ln(r) * cos(" + j + " * phi)";
                    }
                    else if(i==0 && j !=0 && j != max_cos) {
                        u_r += " * cos(" + j + " * phi)";
                    }
                    else if (i !=0 && j != 0 && j == max_sin){
                        u_r += " * ln(r) * sin(" + j + " * phi)";
                    }
                    else if (i !=0 && j != 0 && j != max_sin) {
                        u_r += " * sin(" + j + " * phi)";
                    }
                    //System.out.println("u_r = " + u_r);
                }
            }
        }
        return u_r;
    }
    public static String type_of_derichle(double r_1, double r_2, double x_0, double y_0, String conditional_first ,String conditional_second, String heterogeneous, boolean heterogeneous_exist) {
        String ans = "0";
        if (r_1 == 0 && r_2 != 0) {
            System.out.println("ВНУТРЕНЯЯ ЗАДАЧА");
            r_1 = r_2;
            r_2 = 0;
            conditional_second = "0";
            double[][] koeff_cond_first_in_polar;
            double[][] koeff_cond_second_in_polar;
            //boolean heterogeneous_exist = true;
            if (heterogeneous_exist == true) {
                koeff_cond_first_in_polar = heterogeneous(heterogeneous,x_0,y_0,conditional_first, r_1);
                koeff_cond_second_in_polar = Parsing.conditional_in_polar_coord(conditional_second, r_2, x_0, y_0);//коэфф в разложении граничной функции при соотв радиусе
                ans = Dirichle.ring_and_internal_tasks(koeff_cond_first_in_polar,koeff_cond_second_in_polar,r_1,r_2,x_0,y_0);
                String u_r = print_u_r(koeff_u_r_in_polar_coord(heterogeneous,x_0,y_0));
                System.out.println("u_r 1= " + u_r);
                //temp = u_r;
                ans += u_r;
            }
            else {
                koeff_cond_first_in_polar = Parsing.conditional_in_polar_coord(conditional_first, r_1, x_0, y_0);//коэфф в разложении граничной функции при соотв радиусе
                koeff_cond_second_in_polar = Parsing.conditional_in_polar_coord(conditional_second, r_2, x_0, y_0);
                ans = Dirichle.ring_and_internal_tasks(koeff_cond_first_in_polar,koeff_cond_second_in_polar,r_1,r_2,x_0,y_0);
            }
        }
        else if (r_1 !=0 && r_2 == 0) {
            System.out.println("ВНЕШНЯЯ ЗАДАЧА");
            r_2 = r_1;
            r_1 = 0;
            conditional_first = "0";
            double[][] koeff_cond_second_in_polar;
            double[][] koeff_cond_first_in_polar;
            //boolean heterogeneous_exist = true;
            if (heterogeneous_exist == true) {
                koeff_cond_second_in_polar = heterogeneous(heterogeneous,x_0,y_0,conditional_second, r_2);
                //koeff_cond_first_in_polar = Parsing.conditional_in_polar_coord(conditional_first, r_1, x_0, y_0);//коэфф в разложении граничной функции при соотв радиусе
                ans = Dirichle.external_task(conditional_first,koeff_cond_second_in_polar,r_1,r_2,x_0,y_0);
                String u_r = print_u_r(koeff_u_r_in_polar_coord(heterogeneous,x_0,y_0));
                System.out.println("u_r = " + u_r);
                //temp = u_r;
                ans += u_r;
            }
            else {
                koeff_cond_second_in_polar = Parsing.conditional_in_polar_coord(conditional_second, r_2, x_0, y_0);//коэфф в разложении граничной функции при соотв радиусе
            }
            ans = Dirichle.external_task(conditional_first,koeff_cond_second_in_polar,r_1,r_2,x_0,y_0);
        }
        else {
            System.out.println("В КОЛЬЦЕ");
            double[][] koeff_cond_first_in_polar;
            double[][] koeff_cond_second_in_polar;
            //boolean heterogeneous_exist = true;
            if (heterogeneous_exist == true) {
                koeff_cond_first_in_polar = heterogeneous(heterogeneous,x_0,y_0,conditional_first, r_1);
                koeff_cond_second_in_polar = heterogeneous(heterogeneous,x_0,y_0,conditional_second, r_2);
                ans = Dirichle.ring_and_internal_tasks(koeff_cond_first_in_polar,koeff_cond_second_in_polar,r_1,r_2,x_0,y_0);
                u_r += print_u_r(koeff_u_r_in_polar_coord(heterogeneous,x_0,y_0));
                System.out.println("u_r 1 = " + u_r);
                //temp = u_r;
                ans += u_r;
                }
            else {
                koeff_cond_first_in_polar = Parsing.conditional_in_polar_coord(conditional_first, r_1, x_0,y_0);
                koeff_cond_second_in_polar = Parsing.conditional_in_polar_coord(conditional_second, r_2, x_0, y_0);//коэфф в разложении граничной функции при соотв радиусе
                ans = Dirichle.ring_and_internal_tasks(koeff_cond_first_in_polar,koeff_cond_second_in_polar,r_1,r_2,x_0,y_0);
            }
            //double[][] koeff_cond_first_in_polar = heterogeneous("-xy",0,0,"0", 3);
           // ans = Dirichle.ring_and_internal_tasks(koeff_cond_first_in_polar,koeff_cond_second_in_polar,r_1,r_2,x_0,y_0);
        }

        return ans;
    }

    public static String type_of_neumann(double r_1, double r_2, double x_0, double y_0, String conditional_first ,String conditional_second) {
        String ans = "0";

        double[][]koeff_cond_first_in_polar = Parsing.conditional_in_polar_coord(conditional_first, r_1, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе
        double[][]koeff_cond_second_in_polar = Parsing.conditional_in_polar_coord(conditional_second, r_2, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе
        double[][] koeff_a_and_b = null;
        double[][] new_koeff_a_and_b = null;

        if (r_1 != 0.0 && r_2 != 0.0){
            if (koeff_cond_first_in_polar[0][0]*r_1 == koeff_cond_second_in_polar[0][0]*r_2) {
                System.out.println("КОЛЬЦО Необходимое условие выполнено");
                koeff_cond_first_in_polar = Parsing.conditional_in_polar_coord(conditional_first, r_1, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе
                koeff_cond_second_in_polar = Parsing.conditional_in_polar_coord(conditional_second, r_2, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе
                double[][]koeff_first = Neumann.koeff_a_for_ring_task(koeff_cond_first_in_polar, r_1, r_2, false);
                double[][]koeff_second = Neumann.koeff_a_for_ring_task(koeff_cond_second_in_polar, r_1, r_2, false);
                ans = Neumann.print_res_for_ring_task(koeff_first, r_1, r_2, false) +" + " +Neumann.print_res_for_ring_task(koeff_second, r_1, r_2, true);
            }
            else {
                System.out.println("КОЛЬЦО Необходимое условие не выполнено");
                ans = "Необходимое условие не выполнено";
            }
        }
        else if (r_1 == 0.0 && r_2 != 0.0) {//ВНУТРЕНЯЯ ЗАДАЧА
            if (koeff_cond_second_in_polar[0][0] != 0) {
                System.out.println("ВНУТРЕНЯЯ Необходимое условие не выполнено");
                ans = "Необходимое условие не выполнено";
            }
            else {
                System.out.println("ВНУТРЕНЯЯ Необходимое условие выполнено");
                koeff_cond_second_in_polar = Parsing.conditional_in_polar_coord(conditional_first, r_2, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе
                koeff_a_and_b = Neumann.koeff_a_for_non_ring_task(koeff_cond_second_in_polar, r_2);
                new_koeff_a_and_b = Neumann.internal_task(koeff_a_and_b,r_2);
                //print_res_for_internal(new_koeff_a_and_b);
                ans = Neumann.print_res_for_internal(new_koeff_a_and_b);
            }
        }
        else if (r_1 != 0.0 && r_2 == 0.0) {//ВНЕШНЯЯ ЗАДАЧА
            if (koeff_cond_first_in_polar[0][0] != 0) {
                System.out.println("ВНЕШНЯЯ Необходимое условие не выполнено");
                ans = "Необходимое условие не выполнено";
            }
            else {
                System.out.println("ВНЕШНЯЯ Необходимое условие выполнено");
                koeff_cond_first_in_polar = Parsing.conditional_in_polar_coord(conditional_second, r_1, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе
                koeff_a_and_b = Neumann.koeff_a_for_non_ring_task(koeff_cond_first_in_polar, r_1);
                new_koeff_a_and_b = Neumann.external_task(koeff_a_and_b,r_1);
                ans = Neumann.print_res_for_external(new_koeff_a_and_b);
            }
        }
        return ans;
    }

    public static int[] max_power(double[][] koeff_heterogeneous) {
        int[] power = new int[2];
        for(int i = 0; i < koeff_heterogeneous.length; i++) {
            for (int j = 0; j < koeff_heterogeneous[i].length; j++) {
                if (koeff_heterogeneous[i][j] != 0.0) {
                    if (i == 0) {
                        power[0] = j;
                    }
                    else {
                        power[1] = j;
                    }
                }
            }
        }
        return power;
    }
    public static int[] after_check = new int[3];
    public static int[] check(String heterogeneous) {

        String pattern = "-?\\d+x\\^([02468])-?\\d+y\\^(\\1)";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(heterogeneous);

        if (matcher.find()) {
            String evenDegree = matcher.group(1);
            System.out.println("Степень: " + evenDegree);
            after_check[0] = 1;
            after_check[1] = Integer.parseInt(evenDegree);
            String matched = matcher.group(1);
            String remainingString = heterogeneous.replace(matched, "").trim();
            if (!remainingString.isEmpty()) {
                System.out.println("В строке есть другие слагаемые: " + remainingString);
                after_check[2] = 1;
            } else {
                System.out.println("В строке нет других слагаемых");
            }
        } else {
            System.out.println("Строка не соответствует шаблону");
            after_check[0] = 0;
            after_check[1] = 0;
        }
    return after_check;
    }
    public static boolean remove = false;
   public static double[][] koeff_u_r_in_polar_coord(String heterogeneous, double x_0, double y_0){
       double[][]koeff_heterogeneous = Parsing.conditional_in_polar_coord(heterogeneous, 1, x_0,y_0);
       double[][] koeff_u_r = new double[koeff_heterogeneous.length][koeff_heterogeneous[0].length];
       int[] check = check(heterogeneous);
       double a = 0;
       String u_r = "";

       if (check[0] == 0) {
           max_cos = max_power(koeff_heterogeneous)[0];
       }
       else {
           max_cos = check[1];
           remove = true;
//           String pattern = "(-?\\d+x\\^([02468])-?\\d+y\\^(\\2))";
//
//           Pattern compiledPattern = Pattern.compile(pattern);
//           Matcher matcher = compiledPattern.matcher(heterogeneous);
//
//           String resultString = matcher.replaceAll("");
//           System.out.println("Результат после удаления вхождения: " + resultString.trim());
       }
       max_sin = max_power(koeff_heterogeneous)[1];
       for(int i = 0; i < koeff_heterogeneous.length; i++) {
           for(int j = 0; j < koeff_heterogeneous[i].length; j++) {
               if (koeff_heterogeneous[i][j] != 0.0) {
                   if (j == 0) {
                       a = koeff_heterogeneous[i][j]/2;
                       koeff_u_r[i][j] = a;
                       u_r += "(" + a +") * r^(" + 2*koeff_heterogeneous[0].length + ")";
                       break;
                   }
//                   else if (j == 1) {
//                       a = koeff_heterogeneous[i][j]/2;
//                       koeff_u_r[i][j] = a;
//                       u_r += "(" + a +") * r^(" + j + ") + ";
//                      // max_cos = j;
//                   }
                   else {
                           if (i == 0) {
                               if ((Math.pow(max_cos, 2) - Math.pow(j,2)) == 0) {
                                   a = koeff_heterogeneous[i][j]/(2*j);
                               }
                               else {
                                   a = koeff_heterogeneous[i][j] / (Math.pow(max_cos, 2) - Math.pow(j,2));//UPD 27/03/23 21:43
                               }
                           }
                           else {
                               if ((Math.pow(max_sin, 2) - Math.pow(j,2)) == 0) {
                                   a = koeff_heterogeneous[i][j]/(2*j);
                               }
                               else {
                                   a = koeff_heterogeneous[i][j] / (Math.pow(max_sin, 2) - Math.pow(j, 2));//UPD 27/03/23 21:43
                               }
                           }
                           koeff_u_r[i][j] = a;
                           u_r += " + (" + a + ") * r^" + j;
                   }
                   System.out.println("u_r = " + u_r);
               }
           }
       }
       System.out.println("koeff_heterogeneous");
       for(int i = 0; i < koeff_heterogeneous.length; i++) {
           for(int j = 0; j < koeff_heterogeneous[0].length; j++) {
               System.out.print(koeff_heterogeneous[i][j] + "  ");
           }
           System.out.println();
       }
       System.out.println("Koeff_u_r");
       for(int i = 0; i < koeff_u_r.length; i++) {
           for(int j = 0; j < koeff_u_r[0].length; j++) {
               System.out.print(koeff_u_r[i][j] + "  ");
           }
           System.out.println();
       }
       //after_check[0] = 0;
       //after_check[1] = 0;
       return koeff_u_r;
   }
    public static double[][] heterogeneous (String heterogeneous, double x_0, double y_0, String conditional, double r) {
        double[][]koeff_heterogeneous = Parsing.conditional_in_polar_coord(heterogeneous, 1, x_0,y_0);
        double[][] koeff_u_r = koeff_u_r_in_polar_coord(heterogeneous, x_0,y_0);

        double[][]koeff_cond_in_polar = Parsing.conditional_in_polar_coord(conditional, r, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе
        //double[][]final_solution = new double[koeff_heterogeneous.length][koeff_heterogeneous[0].length];
        if (koeff_cond_in_polar[0].length > koeff_heterogeneous[0].length) {
            koeff_heterogeneous = Dirichle.extent_for_same_size(koeff_heterogeneous, koeff_cond_in_polar[0].length);
        }
        else {
            koeff_cond_in_polar = Dirichle.extent_for_same_size(koeff_cond_in_polar, koeff_heterogeneous[0].length);
        }
        //boolean ln_sin = false;
        //boolean ln_cos = false;
        for(int i = 0; i < koeff_cond_in_polar.length; i++) {//29/03/23
            for(int j = 0; j < koeff_cond_in_polar[i].length; j++) {
                if (koeff_heterogeneous[i][j] != 0.0) {
                    if (j !=0 ) {
//                        if(i==0 && j==1 && remove == true) {
//                            koeff_cond_in_polar[0][1] -= koeff_u_r[i][j] * Math.pow(r, 1) * Math.log(r);
//                        }
//                        else
                            if (i==0 && j==max_cos) {
                            koeff_cond_in_polar[i][j] -= koeff_u_r[i][j] * Math.pow(r, j) * Math.log(r);
                        }
                        else if (i==0 && j!=max_cos && after_check[0]==1){
                            koeff_cond_in_polar[i][j] -= koeff_u_r[i][j] * Math.pow(r, max_cos);//29/03/23
                        }
                        else if (i==1 && j==max_sin) {
                            koeff_cond_in_polar[i][j] -= koeff_u_r[i][j] * Math.pow(r, j) * Math.log(r);
                        }
                        else {
                            koeff_cond_in_polar[i][j] -= koeff_u_r[i][j] * Math.pow(r, j);
                        }
                        //System.out.println("u_r = " + koeff_u_r[i][j] * Math.pow(r, j) * Math.log(r));
                        System.out.println("u = " + koeff_cond_in_polar[i][j]);
                    }
                    else {
                        koeff_cond_in_polar[i][j] -= koeff_u_r[i][j] * Math.pow(r, j);
                        //System.out.println("u_r = " + koeff_u_r[i][j] * Math.pow(r, 2));
                        System.out.println("u = " + koeff_cond_in_polar[i][j]);
                    }
                }
            }
        }

        System.out.println("koeff_cond_in_polar");
        for(int i = 0; i < koeff_cond_in_polar.length; i++) {
            for(int j = 0; j < koeff_cond_in_polar[i].length; j++) {
                System.out.print(koeff_cond_in_polar[i][j] + "  ");
            }
            System.out.println();
        }

        return koeff_cond_in_polar;
    }

    //TO_DO:
    //НЕОДНОРОДНЫЙ СЛУЧАЙ
    public static void main(String[] args) {
//ОТКЛАДКА НЕОДНОРОДНОГО СЛУЧАЯ
//        double x_0 = 0.0;
//        double y_0 = 0.0;
//        String conditional_first = "0";
//        String conditional_second = "0";
//        String heterogeneous = "-xy";
//        double r_1 = 0.0;
//        double r_2 = 3.0;
//        boolean heterogeneous_exist = true;
//        String ans = type_of_derichle(r_1,r_2,x_0,y_0,conditional_first,conditional_second, heterogeneous, heterogeneous_exist);
//        System.out.println(ans);

        //НЕОДНОРОДНЫЙ СЛУЧАЙ ВНЕ ФУНКЦИЙ
//        double[][]koeff_heterogeneous = Parsing.conditional_in_polar_coord(heterogeneous, 1, x_0,y_0);
//        double[][] koeff_u_r = new double[koeff_heterogeneous.length][koeff_heterogeneous[0].length];
//        double a = 0;
//        String u_r = "";
//        for(int i = 0; i < koeff_heterogeneous.length; i++) {
//            for(int j = 0; j < koeff_heterogeneous[i].length; j++) {
//                if (koeff_heterogeneous[i][j] != 0.0) {
//                    if (j == 0) {
//                        a = koeff_heterogeneous[i][j]/2;
//                        koeff_u_r[i][j] = a;
//                        u_r += a +" * r^" + 2;
//                    }
//                    else {
//                        a = koeff_heterogeneous[i][j] / (2 * j);
//                        koeff_u_r[i][j] = a;
//                        u_r += a +" * r^" + j;
//                    }
////                    koeff_u_r[i][j] = a;
////                    u_r += a +" * r^" + j;
////                    if (i == 0) {
////                        u_r += " * cos"
////                    }
//                    System.out.println("u_r = " + u_r);
//                }
//            }
//        }
//
//
//        double[][]koeff_cond_first_in_polar = Parsing.conditional_in_polar_coord(conditional_first, r_1, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе
//        double[][]final_solution = new double[koeff_heterogeneous.length][koeff_heterogeneous[0].length];
//        koeff_cond_first_in_polar = Dirichle.extent_for_same_size(koeff_cond_first_in_polar, koeff_heterogeneous[0].length);
//        for(int i = 0; i < koeff_heterogeneous.length; i++) {
//            for(int j = 0; j < koeff_heterogeneous[i].length; j++) {
//                if (koeff_heterogeneous[i][j] != 0.0) {
//                    koeff_cond_first_in_polar[i][j] -= koeff_u_r[i][j]*Math.pow(r_1,j)* Math.log(r_1);
//                    System.out.println("u_r = " + koeff_u_r[i][j]*Math.pow(r_1,j)* Math.log(r_1));
//                    System.out.println("u = " + koeff_cond_first_in_polar[i][j]);
//                }
//            }
//        }

        //type_of_derichle(3,0,0,0,conditional_first,"x");

//        double[][]koeff_cond_second_in_polar = Parsing.conditional_in_polar_coord(conditional_second, r_2, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе

       // type_of_neumann(r_1, r_2, x_0, y_0, conditional_first, conditional_second);

       //if(true) return;

        JFrame mForm = new JFrame("DirihleNeumann");

        JLabel rules = new JLabel();
        rules.setText("<html><pre>Для внешней задачи введите r_2 = 0 и 2-ое граниченое условие, \n для внутренней - r_1 = 0 и 1-ое граничное условие</pre></html>");

        JLabel rules_2 = new JLabel();
        rules_2.setText("<html><pre>Вводить в виде: xy^2+x/(x^2+y^2)^2.0+ln3/(x^2+y^2)^-2.5</pre></html>");

        JLabel rules_3 = new JLabel();
        rules_3.setText("");

        JLabel empty_line = new JLabel("");   // <--- empty label to effect next row
        empty_line.setPreferredSize(new Dimension(1000,0));

        JCheckBox implicit = new JCheckBox();
        implicit.setText("Неявно: ");
        implicit.setPreferredSize(new Dimension(100, 25));

        JLabel x_offset = new JLabel();
        x_offset.setText("x_0: ");
        JTextArea x_0_offset = new JTextArea();
        x_0_offset.setPreferredSize(new Dimension(25,25));
        x_0_offset.setText("0");
        //x_0_offset.getText();

        JLabel y_offset = new JLabel();
        y_offset.setText("y_0: ");
        JTextArea y_0_offset = new JTextArea();
        y_0_offset.setBounds(40,90,85,25);
        y_0_offset.setPreferredSize(new Dimension(25,25));
        y_0_offset.setText("0");

        JLabel empty_line_between = new JLabel("");   // <--- empty label to space between elements
        empty_line_between.setPreferredSize(new Dimension(50,0));

        JLabel r_1_label = new JLabel();
        r_1_label.setText("r_1: ");
        JTextArea r_1_num = new JTextArea();
        r_1_num.setBounds(20,90,25,25);
        r_1_num.setPreferredSize(new Dimension(25,25));

        JLabel r_2_label = new JLabel();
        r_2_label.setText("r_2: ");
        JTextArea r_2_num = new JTextArea();
        r_2_num.setBounds(40,90,85,25);
        r_2_num.setPreferredSize(new Dimension(25,25));

        JLabel u_1_label = new JLabel();
        u_1_label.setText("Первое граничное условие: ");
        JTextArea u_1_num = new JTextArea();
        u_1_num.setBounds(20,90,25,25);
        u_1_num.setPreferredSize(new Dimension(150,25));
        u_1_num.setText("0");

        JLabel u_2_label = new JLabel();
        u_2_label.setText("Второе граничное условие: ");
        JTextArea u_2_num = new JTextArea();
        u_2_num.setBounds(40,90,85,25);
        u_2_num.setPreferredSize(new Dimension(150,25));
        u_2_num.setText("0");

        JLabel het = new JLabel();
        het.setText("Неоднородность: ");
        het.setPreferredSize(new Dimension(150,25));
        JTextArea het_value = new JTextArea();
        het_value.setBounds(40,90,85,25);
        het_value.setPreferredSize(new Dimension(150,25));
        het_value.setText("0");

        JLabel error = new JLabel();
        error.setText("");

        JButton answer = new JButton("Решение Дирихле");
        answer.setBounds(40,120,85,20);

        JButton answer_2 = new JButton("Решение Неймана");
        answer.setBounds(40,120,85,20);

        JTextArea het_solution = new JTextArea();
        het_solution.setText("");
        het_solution.setPreferredSize(new Dimension(400, 50));
        het_solution.setLineWrap(true);
        het_solution.setWrapStyleWord(true);

        JTextArea solution = new JTextArea();
        solution.setText("");
        solution.setPreferredSize(new Dimension(400, 200));
        solution.setLineWrap(true);
        solution.setWrapStyleWord(true);

        mForm.setLayout(new FlowLayout (FlowLayout.CENTER));

        mForm.add(rules_2);
        mForm.add(empty_line);
        mForm.add(rules_3);
        mForm.add(empty_line);
        mForm.add(rules);
        mForm.add(empty_line);
        mForm.add(implicit);
        mForm.add(empty_line_between);
        mForm.add(x_offset);
        mForm.add(x_0_offset);
        mForm.add(y_offset);
        mForm.add(y_0_offset);
        mForm.add(empty_line_between);
        mForm.add(r_1_label);
        mForm.add(r_1_num);
        mForm.add(r_2_label);
        mForm.add(r_2_num);
        mForm.add(empty_line);
        mForm.add(u_1_label);
        mForm.add(u_1_num);
        mForm.add(empty_line);
        mForm.add(u_2_label);
        mForm.add(u_2_num);
        mForm.add(empty_line);
        mForm.add(het);
        mForm.add(het_value);
        mForm.add(empty_line);
        mForm.add(error);
        mForm.add(empty_line);
        mForm.add(answer);
        mForm.add(empty_line);
        mForm.add(answer_2);
        mForm.add(new JScrollPane(solution));
        mForm.add(empty_line);
        mForm.add(het_solution);
        mForm.setPreferredSize(new Dimension(475,550));
        mForm.setLocationRelativeTo(null);
        mForm.pack();
        mForm.setVisible(true);

        //if(true)return;


        //String conditional_second = "4y^2-2x-x^2y";
        //String conditional_first = "xy/(x^2+y^2)^-2.5+xy+y-y/(x^2+y^2)+5";

        //String conditional_second = "0.03125x^2+0.125";

        //System.out.println("x_0: " + x_0 + "  y_0: " + y_0);

        answer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ans = "";
                u_r = "";
                String conditional_first = u_1_num.getText().trim().toString();
                String conditional_second = u_2_num.getText().trim().toString();
                String heterogeneous = het_value.getText().trim().toString();
                double x_0 = 0;
                double y_0 = 0;
                double r_1 = 0;
                double r_2 = 0;
                if (implicit.isSelected() == false) {
                    r_1 = Double.parseDouble(r_1_num.getText());//когда r_1 = 0, то это внутренняя задача
                    r_2 = Double.parseDouble(r_2_num.getText());//когда r_2 = inf, то это внешняя задача
                    x_0 = Double.parseDouble(x_0_offset.getText());
                    y_0 = Double.parseDouble(y_0_offset.getText());
                }
                else {
                    x_0 = 1 * Double.parseDouble(x_0_offset.getText())/2;
                    y_0 = 1 * Double.parseDouble(y_0_offset.getText())/2;
                    //System.out.println("x_0  " + x_0);
                    //System.out.println("y_0  " + y_0);
                    if (Integer.parseInt(r_1_num.getText()) != 0) {
                        r_1 = Math.sqrt(Integer.parseInt(r_1_num.getText()) + Math.pow(x_0,2) + Math.pow(y_0,2));
                        System.out.println("r_1  " + r_1);
                    }
                    if (Integer.parseInt(r_2_num.getText()) != 0) {
                        r_2 = Math.sqrt(Integer.parseInt(r_2_num.getText()) + Math.pow(x_0,2) + Math.pow(y_0,2));
                        System.out.println("r_2  " + r_2);
                    }
                }
                boolean heterogeneous_exist = false;
                if ((heterogeneous) != "0") {
                    heterogeneous_exist = true;
                }
                ans = type_of_derichle(r_1, r_2, x_0, y_0, conditional_first, conditional_second, heterogeneous, heterogeneous_exist);
                solution.setText(ans);
                if (heterogeneous_exist == true) {
                    het_solution.setText("Частное решение = " + u_r);
                }
                //JOptionPane.showMessageDialog(null, ans);
            }
        });

        answer_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String conditional_first = u_1_num.getText().trim().toString();
                String conditional_second = u_2_num.getText().trim().toString();
                double x_0 = 0;
                double y_0 = 0;
                double r_1 = 0;
                double r_2 = 0;
                if (implicit.isSelected() == false) {
                    r_1 = Double.parseDouble(r_1_num.getText());//когда r_1 = 0, то это внутренняя задача
                    r_2 = Double.parseDouble(r_2_num.getText());//когда r_2 = inf, то это внешняя задача
                    x_0 = Double.parseDouble(x_0_offset.getText());
                    y_0 = Double.parseDouble(y_0_offset.getText());
                }
                else {
                    x_0 = 1 * Double.parseDouble(x_0_offset.getText())/2;
                    y_0 = 1 * Double.parseDouble(y_0_offset.getText())/2;
                    //System.out.println("x_0  " + x_0);
                    //System.out.println("y_0  " + y_0);
                    if (Integer.parseInt(r_1_num.getText()) != 0) {
                        r_1 = Math.sqrt(Integer.parseInt(r_1_num.getText()) + Math.pow(x_0,2) + Math.pow(y_0,2));
                        System.out.println("r_1  " + r_1);
                    }
                    if (Integer.parseInt(r_2_num.getText()) != 0) {
                        r_2 = Math.sqrt(Integer.parseInt(r_2_num.getText()) + Math.pow(x_0,2) + Math.pow(y_0,2));
                        System.out.println("r_2  " + r_2);
                    }
                }
                String ans = type_of_neumann(r_1, r_2, x_0, y_0, conditional_first, conditional_second);
                solution.setText(ans);
                //JOptionPane.showMessageDialog(null, ans);
            }
        });

        implicit.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    r_1_label.setText("C_1");
                    r_2_label.setText("C_2");
                    x_offset.setText("A");
                    y_offset.setText("B");
                    rules.setText("<html><pre>Для внешней задачи введите C_2 = 0 и 2-ое граниченое условие, \n для внутренней - C_1 = 0 и 1-ое граничное условие</pre></html>");
                    rules_3.setText("<html><pre>\nx^2+y^2+A*x+B*y=C_1/C_2</pre></html>");
                }
                else {
                    r_1_label.setText("r_1");
                    r_2_label.setText("r_2");
                    x_offset.setText("x_0");
                    y_offset.setText("y_0");
                    rules.setText("<html><pre>Для внешней задачи введите r_2 = 0 и 2-ое граниченое условие, \n\tдля внутренней - r_1 = 0 и 1-ое граничное условие</pre></html>");
                    rules_3.setText("");
                }
            }
        });


//        else if (r_1 == 0 && r_2 !=0 ) {//ВНЕШНЯЯ ЗАДАЧА
//            System.out.println("\n" + print_result_for_internal_task(array_of_const));
//        }

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