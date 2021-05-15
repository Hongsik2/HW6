# 근사 알고리즘

## 근사화를 이용한 NP-완전문제의 해결

### 작업 스케줄링

- n개의 작업
- 각 작업의 수행 시간 t(i)
- m개의 동일한 수행 능력을 가진 기계
- 모든 작업이 가장 빨리 종료되도록 작업을 기계에 배정하는 문제.

#### 근사화 방법

- **그리디 알고리즘**을 사용한다

#### 최적해

- 한정된 양의 작업(작은 양)이라면 전수조사(*BruteForce*)를 해서 최적해를 구할 수 있다.

### 시간 복잡도

#### 그리디 알고리즘

- n개의 작업을 기계에 배정해야하고
- 작업 시간이 저장된 배열 L[m] 을 탐색해야하므로
- **n * O(m) +O(m) = O(mn) 이다.** *(m은 기계의 개수, n은 작업의 개수)*

#### 전수조사

- n개의 작업을 m개의 기계에 배정하는 경우의 수는 m^n개 이고
- 작업시간이 저장된 배열 L[m^n] 을 탐색해야하므로
- **n * O(m) * O(m^n) + O(m^n) = O(m^n) 이다.** *(m은 기계의 개수, n은 작업의 개수)*



### 근사비율

- 근사비율은 근사해의 값과 최적해의 값의 비율로서, 1.0에 가까울수록 정확도가 높은 알고리즘이다.
- 일반적인 문제에서는 최적해를 구할수 없어 '간접적인' 최적해를 구해야 하지만 여기서는 전수조사를 이용해 최적해를 구하였다.

#### 이론적인 근사비율

- 근사해를 OPT', 최적해를 OPT 라고 한다.
- OPT' <= 2*OPT 이다. (근사해는 최적해의 2배를 넘지 않는다.)

![근사비율(PPT 참고)](https://user-images.githubusercontent.com/80087069/118380820-d80e7680-b61f-11eb-9853-5f9a7d93204d.png)

#### 실제 실행 결과

- 작업의 개수를 4,8,16 개로 바꾸어가며 100회씩 반복실행했다.
- 기계의 개수는 2개로 고정했다.
- 결과값은 다음과 같았다.

| 작업의 개수 | 4     | 8     | 16     |
| ----------- | ----- | ----- | ------ |
| 최대값      | 1.424 | 1.251 | 1.123  |
| 평균값      | 1.089 | 1.076 | 1.0411 |
| 최소값      | 1     | 1     | 1      |

- 여기서 알 수 있는 것은 작업의 개수가 많아지면 많아질수록 근사 비율이 작아진다는 것이었다.



## 코드

```java
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

```



