import java.util.*;

public class JobScheduling {

    public static int Bruteforce(int[] t, int m) {
        int[] s = new int[t.length];                //s[i] = i번째 작업이 들어가게 되는 기계의 번호를 저장한 배열
        int[] L = new int[m];                       //기계 Mj에 배정된 마지막 작업의 종료 시간

        for (int i = 0; i < t.length; i++) {
            s[i] = 0;
        }
        double count = Math.pow(m, t.length);
        int[] endTimeset = new int[(int) count];    //각 실행(i회차)에서 마지막 작업의 종료 시간

        for (int i = 0; i < (int) count; i++) {

            for (int j = 0; j < m; j++) {
                L[j] = 0;
            }

            for (int j = 0; j < t.length; j++) {
                L[s[j]] = L[s[j]] + t[j];           //j번째 작업을 j번째 작업이 들어가는 기계에 배정
            }

            for (int j = 0; j < m; j++) {
                if (L[j] > endTimeset[i]) {
                    endTimeset[i] = L[j];           //각 실행에서 가장 늦은 작업 종료시간을 i회차 실행시간에 저장
                }
            }


            s[0]++;
            for (int j = 0; j < t.length; j++) {    //작업이 기계에 들어가는 번호 변경
                if (s[j] > m - 1) {
                    s[j] = 0;
                    if (j + 1 < t.length) {
                        s[j + 1]++;
                    }
                }
            }
        }

        int endTime = Integer.MAX_VALUE;
        for (int i = 0; i < count; i++) {           //모든 경우의 수에서의 실행 값 중 가장 작은 값 반환
            if (endTimeset[i] < endTime) {
                endTime = endTimeset[i];
            }
        }


        return endTime;
    }

    public static int ApproxJobscheduling(int[] t, int m) {


        int[] L = new int[m];                       //기계 Mj에 배정된 마지막 작업의 종료 시간


        for (int i = 0; i < m; i++) {
            L[i] = 0;
        }
        for (int i = 0; i < t.length; i++) {
            int min = 0;
            for (int j = 1; j < m; j++) {
                if (L[j] < L[min]) {
                    min = j;
                }
            }
            L[min] = L[min] + t[i];
        }

        int endTime = 0;
        for (int i = 0; i < m; i++) {
            if (L[i] > endTime) {
                endTime = L[i];
            }
        }


        return endTime;
    }


    public static void main(String[] args) {
        int q;                      //작업의 개수

        Scanner scanner = new Scanner(System.in);
        System.out.print("작업의 개수를 입력하세요 : ");
        q = scanner.nextInt();

        int m = 2;                  //기계의 개수 : 2

        Random random = new Random();

        int[] t = new int[q];       //각 작업의 수행 시간을 저장한 배열

        for (int i = 0; i < q; i++) {
            t[i] = random.nextInt(9) + 1;
        }

        int approxEndtime = JobScheduling.ApproxJobscheduling(t, m);
        int bruteEndtime = JobScheduling.Bruteforce(t, m);

        System.out.printf("근사화 작업 스케줄링 : %d \n", approxEndtime);
        System.out.printf("전수조사 작업 스케줄링 : %d", bruteEndtime);
    }
}
