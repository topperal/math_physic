import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dirichle {
    static double[][] create_matrix(double r,double[][]koeff_cond, double[][] koeff_another) {
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
                            matrix_first_for_gauss[i][i * 2 + 1] = Math.pow(r, -1*i);
                        }
                    }
                    if (j == 1) {
                        if (i !=0) {
                            matrix_first_for_gauss[i + all_columns][i * 2 + all_columns * 2] = Math.pow(r, i);
                            matrix_first_for_gauss[i + all_columns][i * 2 + 1 + all_columns * 2] = Math.pow(r, -1*i);
                        }
//                        if (i == 0) {
////                            matrix_first_for_gauss[all_columns][all_columns * 2] = 1;
////                            matrix_first_for_gauss[all_columns][all_columns * 2 + 1] = Math.log(r);
//                        } else {
//                            matrix_first_for_gauss[i + all_columns][i * 2 + all_columns * 2] = Math.pow(r, i);
//                            matrix_first_for_gauss[i + all_columns][i * 2 + 1 + all_columns * 2] = Math.pow(r, -1*i);
//                        }
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

    static String print_result_for_ring (double[] array_of_const) {
        String final_str = "";
        int k = array_of_const.length/2;
        boolean first_element= false;
        boolean const_exist = false;
        int first_is_null = 0;
        if (array_of_const[0] == 0) {
            final_str += " (" + array_of_const[1] + ") ln(r) + ";
            first_is_null += 2;
        }
        for(int i = 0+first_is_null; i < array_of_const.length; i++) {
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
                        final_str += "(" + array_of_const[i] + ") * r^(" + step + ") * cos (" + step + " * phi) +";
                        first_element = true;
                    } else if (i <= k && first_element == true) {
                        int step = i / 2;
                        final_str += "(" + array_of_const[i] + ") * r^(-" + step + ") * cos (" + step + " * phi) +";
                        first_element = false;
                    }
                    if (i > k && first_element == false) {
                        int step = i / 2 - k / 2;
                        final_str += " (" + array_of_const[i] + ") * r^(" + step + ") * sin (" + step + " * phi) + ";
                        first_element = true;
                    } else if (i > k && first_element == true) {
                        int step = i / 2 - k / 2;
                        final_str += " (" + array_of_const[i] + ") * r^(-" + step + ") * sin (" + step + " * phi) +";
                        first_element = false;
                    }
                }
            }
            if (first_element == true && array_of_const[i] == 0) {
                first_element = false;
            }
        }

        return final_str;
    }

    static String print_result_for_internal_task (double[] array_of_const) {
        String final_str = "";
        int k = array_of_const.length/2;
        for(int i = 0; i < array_of_const.length; i++) {
            if (array_of_const[i] != 0) {
                if (i==0) {
                    final_str += "(" + array_of_const[i] + ")";
                }
                else {
                    if (i <= k) {
                        int step = i / 2;
                        final_str += "+ (" + array_of_const[i] + ") * r^(" + step + ") * cos (" + step + " * phi)";
                    }
                    if (i > k) {
                        int step = i / 2 - k / 2;
                        final_str += " + (" + array_of_const[i] + ") * r^(" + step + ") * sin (" + step + " * phi) ";
                    }
                }
            }
        }

        return final_str;
    }

    static String print_result_for_external_task (double[] array_of_const) {
        String final_str = "";
        int k = array_of_const.length/2;
        for(int i = 0; i < array_of_const.length; i++) {
            if (array_of_const[i] != 0) {
                if (i==0) {
                    final_str += "(" + array_of_const[i] + ")";
                }
                else {
                    if (i <= k) {
                        int step = i / 2;
                        final_str += " + (" + array_of_const[i] + ") * r^(-" + step + ") * cos (" + step + " * phi)";
                    }
                    if (i > k) {
                        int step = i / 2 - k / 2;
                        final_str += " + (" + array_of_const[i] + ") * r^(-" + step + ") * sin (" + step + " * phi) ";
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

    public static String ring_and_internal_tasks(double[][] koeff_cond_first_in_polar, double[][] koeff_cond_second_in_polar, double r_1, double r_2, double x_0, double y_0) {
        //double[][]koeff_cond_first_in_polar = Parsing.conditional_in_polar_coord(conditional_first, r_1, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе
        //double[][]koeff_cond_second_in_polar = Parsing.conditional_in_polar_coord(conditional_second, r_2, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе

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
        }
        System.out.println("koeff_cond_second_in_polar");
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
        String answer = "";
        if (r_1 != 0 && r_2 !=0) {
            answer = "\n" + print_result_for_ring(array_of_const);
            System.out.println("\n" + print_result_for_ring(array_of_const));
        }
        else if (r_1 != 0 && r_2 == 0) {//ВНУТРЕННЯЯ ЗАДАЧА
            answer = "\n" + print_result_for_internal_task(array_of_const);
            System.out.println("\n" + print_result_for_internal_task(array_of_const));
        }

        return answer;
    }

    static double[][] create_matrix_for_external_task(double r,double[][]koeff_cond) {
        int all_columns = koeff_cond[0].length;
        int all_rows = koeff_cond.length;

        double[][]matrix_first_for_gauss = new double[all_columns*2][all_columns*4];
        //int k = 0;
        for(int i = 0; i < all_columns; i++) {
            for (int j = 0; j < all_rows; j++) {
                if (koeff_cond[j][i]!=0 ) {
                    if (j == 0) {
                        if (i == 0) {
                            matrix_first_for_gauss[j][0] = 1;
                            matrix_first_for_gauss[j][1] = Math.log(r);
                        } else {
                            matrix_first_for_gauss[i][i * 2] = Math.pow(r, i);
                            matrix_first_for_gauss[i][i * 2 + 1] = Math.pow(r, -1*i);
                        }
                    }
                    if (j == 1) {
                        if (i !=0) {
                            matrix_first_for_gauss[i + all_columns][i * 2 + all_columns * 2] = Math.pow(r, i);
                            matrix_first_for_gauss[i + all_columns][i * 2 + 1 + all_columns * 2] = Math.pow(r, -1*i);
                        }
                    }
                }
            }
        }

        for(int i = 0; i< matrix_first_for_gauss.length; i++) {
            for(int j = 0; j < matrix_first_for_gauss[i].length; j++) {
                System.out.print(matrix_first_for_gauss[i][j]+"  ");
            }
            System.out.println();
        }
        return matrix_first_for_gauss;
    }

    static double[] matrix_for_non_singular_external_task(double[][] matrix, double[] matrix_vector){

        double[] need_column = new double[matrix.length];
        double[] need_rows = new double[matrix.length];
        double[][] non_singular = null;
        double[] non_singular_vector = null;
        int dim_ns = 0;
        int half_matrix_len = matrix.length/2;


        for(int i = 0; i< matrix.length; i++) {//ряд
            for(int j = 0; j < matrix[i].length; j++) {//колонка
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
        System.out.println("dim_ns: " + dim_ns);

        non_singular = new double[dim_ns][dim_ns];
        non_singular_vector = new double[dim_ns];

        int row_ns = 0;
        int column_ns = 0;
        for(int i = 0; i< matrix.length; i++) {//ряд
            column_ns = 0;
            if (need_rows[i] == 1){
                for (int j = 0; j < matrix[i].length; j++) {//колонка
                    if (need_column[j] == 1){
                        if (j%2==1 && j !=1) {
                            non_singular[row_ns][column_ns] = matrix[i][j];
                            column_ns++;
                        }
                        else if (j == 0) {
                            non_singular[row_ns][column_ns] = matrix[i][j];
                            column_ns++;
                        }
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
        System.out.println("After lsolse: ");
        for (int i = 0; i < non_singular.length; i++) {
            System.out.println(x[i]);
        }
        //вместо need rows - need columns
        column_ns = 0;
        System.out.println("NEED COLUMN: ");
        for(int j = 0; j < need_column.length; j++) {
            System.out.print(need_column[j]+"  ");
            if (j == 0 && need_column[j] == 1 ) {
                need_column[j] = x[column_ns];
                need_column[j+1] = 0.0;
                column_ns++;
            }
            if(j > 0 && need_column[j] == 1 && j%2!=1) {
                need_column[j] = x[column_ns];
                need_column[j+1] = 0.0;
                column_ns++;
            }
        }
        System.out.println("NEED COLUMN: ");
        for (int i = 0; i < need_column.length; i++) {
            System.out.print(need_column[i]+"  ");
        }

        return need_column;
    }
    public static String external_task(String conditional_first, double[][] koeff_cond_second_in_polar, double r_1, double r_2, double x_0, double y_0) {
        //double[][]koeff_cond_first_in_polar = conditional_in_polar_coord(conditional_first, r_1, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе
        //double[][]koeff_cond_second_in_polar = Parsing.conditional_in_polar_coord(conditional_second, r_2, x_0,y_0);//коэфф в разложении граничной функции при соотв радиусе

        double[][] matrix_for_second_cond = create_matrix_for_external_task(r_2, koeff_cond_second_in_polar);

        System.out.println("koeff_cond_second_in_polar");
        for(int i = 0; i< koeff_cond_second_in_polar.length; i++) {
            for(int j = 0; j < koeff_cond_second_in_polar[i].length; j++) {
                System.out.print(koeff_cond_second_in_polar[i][j]+"  ");
            }
            System.out.println();
        }

        System.out.println("matrix_for_second_cond");
        for(int i = 0; i< matrix_for_second_cond.length; i++) {
            for(int j = 0; j < matrix_for_second_cond[i].length; j++) {
                System.out.print(matrix_for_second_cond[i][j]+"  ");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("DOUBLE MATRIX: ");

        double[][] final_matrix = new double[matrix_for_second_cond.length*2][matrix_for_second_cond[0].length];

        for(int i = 0; i < final_matrix.length/2; i++) {
            for (int j = 0; j < final_matrix[i].length; j++){
                final_matrix[i][j] = 0.0;
                System.out.print(final_matrix[i][j]+"   ");
            }
            System.out.println();
        }

        for(int i = 0; i < matrix_for_second_cond.length; i++) {
            for (int j = 0; j < matrix_for_second_cond[i].length; j++) {
                final_matrix[i][j]=matrix_for_second_cond[i][j];
                System.out.print(final_matrix[i][j]+"  ");
            }
            System.out.println();
        }

        double[] res_for_matrix = new double [koeff_cond_second_in_polar.length*koeff_cond_second_in_polar[0].length*2];

        int counter = 0;

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

        double[] array_of_const = matrix_for_non_singular_external_task(final_matrix,res_for_matrix);
        String answer = "";
        answer = "\n" + print_result_for_external_task(array_of_const);
        System.out.println("\n" + print_result_for_external_task(array_of_const));

        return answer;
    }
}
