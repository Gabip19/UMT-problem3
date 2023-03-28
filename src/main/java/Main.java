public class Main {
    /*
     * avg(B) = avg(C)  -->  sumB / lenB = sumC / lenC  -->  sumB / lenB = (sumA - sumB) / (lenA - lenB)  -->
     * sumB * lenA - sumB * lenB = (sumA - sumB) * lenB  -->  sumB * lenA = sumA * lenB  -->  sumB = sumA * lenB / lenA
     *
     * Now that sumA and lenA will not change, we can determine them in the start then to solve the problem
     * we must only find a pair of sumB and lenB for which sumB = sumA * lenB / lenA
     *
     * One approach is find all sums possible for all lens
     * If we can have a sum s of len elements for a number x then we can obtain a sum of len + 1 elements with sum
     * s + x  -->  if sums[len][s] then sums[len + 1][s + x]
     *
     * Complexity: O(n * n * S) where S is the sum of all numbers in A and n is the length of A
     *  - because of the input data restriction 1 <= n <= 30 and 0 <= A[i] <= 10000 this algorithm performs really well
     *
     * FURTHER OPTIMIZATIONS:
     * Instead of using a byte matrix we can have an array of integers and every bit in an element (32 bits, element is
     * sums[s]) will have the value of 1 if there is a sum s of length len where len is the position of the bit in the
     * element. This way by using bitwise operations we can get rid of the len for loop, reducing the complexity
     * to O(n * S)
     */
    public static boolean canBeSplit(int[] A) {
        // if A less than two elements we won't be able to split it
        if (A.length < 2)
            return false;

        int n = A.length;
        int sumA = 0;
        // computing A total sum
        for (int elem : A) {
            sumA += elem;
        }

        // using a byte matrix to remember the sums we can compute with a specific number of elements
        // where the bit sums[n][s] is 1 if we can obtain the sum 's' with 'n' numbers and 0 otherwise
        byte[][] sums = new byte[n + 1][sumA + 1];

        // filling the matrix by computing the sums with A[i] then
        // using those to find the ones with A[i + 1] and so on
        sums[1][A[0]] = 1;
        for (int i = 1; i < n; i++) {
            // we can't have sums bigger than sumA, so we start at sumA - a[i]
            // we don't want to consider newly added sums twice, so we should traverse in reverse order
            for (int s = sumA - A[i]; s >= 0; s--) {
                // the max length until now is equal to the number of elements iterated,
                // so we can start at i - 1 our search
                for (int len = i - 1; len > 0; len--) {
                    if (sums[len][s] == 1) sums[len + 1][s + A[i]] = 1;
                }
            }
            // mark the sum made up of only the current element as a valid sum too
            sums[1][A[i]] = 1;
        }

        // iterating through all lengths to check if we find one that works with the needed criteria
        for (int len = 1; len < n; len++) {
            if ((len * sumA) % n == 0 && sums[len][len * sumA / n] == 1) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(canBeSplit(new int[]{10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000}));
    }
}