package discount_offers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class discount_offers {

	private static NumberFormat nf = NumberFormat.getInstance();

	{
		nf.setMinimumFractionDigits(2);
	}

	public static void main(String[] args) throws Exception {

		discount_offers d = new discount_offers();

		String filepath = args[0];

		if (args.length < 1) {
			return;
		}

		try {
			InputStream is = discount_offers.class.getClassLoader()
					.getResourceAsStream(filepath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				d.process(line);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Float process(String line) throws Exception {
		Map<String, List<String>> data = splitRecord(line);

		List<String> customers = data.get("customers");
		List<String> products = data.get("products");

		int custSize = customers.size();
		int productSize = products.size();

		Float[][] scoreMatrix = new Float[custSize][productSize];

		for (int i = 0; i < customers.size(); i++) {
			for (int j = 0; j < products.size(); j++) {
				scoreMatrix[i][j] = calculateScoreForOne(customers.get(i),
						products.get(j));
			}
		}

		// System.out.println(nf.format(calculateScore(hungarianMatrix)));
		return calculateScore(scoreMatrix);
	}

	private Float[][] reverseMatrixIfNeeded(Float[][] hungarianMatrix) {
		int rows = hungarianMatrix.length;
		int cols = hungarianMatrix[0].length;
		if (rows > cols) {
			Float[][] reverseMatrix = reverseMatrix(hungarianMatrix);
			return reverseMatrix;
		} else {
			return hungarianMatrix;
		}
	}

	private Float[][] reverseMatrix(Float[][] hungarianMatrix) {
		int rows = hungarianMatrix.length;
		int cols = hungarianMatrix[0].length;
		Float[][] reverseMatrix = new Float[cols][rows];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				reverseMatrix[j][i] = hungarianMatrix[i][j];
			}
		}
		return reverseMatrix;
	}

	Float calculateScore(Float[][] scoreMatrix) {

		// Reverse the matrix to make sure rows <= cols
		scoreMatrix = reverseMatrixIfNeeded(scoreMatrix);
		int rows = scoreMatrix.length;

		// Step1: convert the matrix from finding biggest to finding smallest
		Float[][] offsetMatrix = convertToFindingSmallestMatrix(scoreMatrix);

		// Step2: Make the matrix square in order to apply Hungarian Algorithm
		Float[][] squareMatrix = convertToSquareMatrixIfNeeded(offsetMatrix);

		List<Integer> possibleResult = findingMostOptimalPathByHungarianAlgorithm(squareMatrix);

		// Calculate score on the original matrix based on the result of
		// Hungarian Algorithm
		float ss = 0;
		for (int i = 0; i < rows; i++) {
			ss += scoreMatrix[i][possibleResult.get(i)];
		}

		return ss;
	}

	private Float[][] convertToSquareMatrixIfNeeded(Float[][] scoreMatrix) {

		int rows = scoreMatrix.length;
		int cols = scoreMatrix[0].length;

		if (rows == cols) {
			return scoreMatrix;
		} else {

			Float[][] squareMatrix = new Float[cols][cols];
			Float stub = 0f;

			for (int i = 0; i < cols; i++) {

				for (int j = 0; j < cols; j++) {

					if (i < rows) {
						// Transfer existing matrix
						squareMatrix[i][j] = scoreMatrix[i][j];
					} else {
						// Transfer matrix stub record
						squareMatrix[i][j] = stub;
					}
				}

			}

		}

		return scoreMatrix;
	}

	private List<Integer> findingMostOptimalPathByHungarianAlgorithm(
			Float[][] hungarianMatrix) {

		// Step 1
		hungarianMatrix = offsetAllRow(hungarianMatrix);

		// Step 2
		hungarianMatrix = offsetAllCol(hungarianMatrix);

		// Test Result
		List<Integer> possibleResult = fillUpResult(new ArrayList<Integer>(),
				hungarianMatrix);
		if (possibleResult != null) {
			return possibleResult;
		}

		// No luck, we continue step 3 & 4 to further transform matrix

		// Step 3
		int[][] markPointCoordinator = new int[0][0];
		int[][] unmarkPointCoordinator = new int[0][0];

		// Step 4
		Float minimumUnmark = Float.MAX_VALUE;
		for (int i = 0; i < unmarkPointCoordinator.length; i++) {
			int row = unmarkPointCoordinator[i][0];
			int col = unmarkPointCoordinator[i][1];
			if (hungarianMatrix[row][col] < minimumUnmark) {
				minimumUnmark = hungarianMatrix[row][col];
			}
		}

		for (int i = 0; i < unmarkPointCoordinator.length; i++) {
			int row = unmarkPointCoordinator[i][0];
			int col = unmarkPointCoordinator[i][1];
			hungarianMatrix[row][col] = hungarianMatrix[row][col]
					- minimumUnmark;
		}

		for (int i = 0; i < markPointCoordinator.length; i++) {
			int row = markPointCoordinator[i][0];
			int col = markPointCoordinator[i][1];
			hungarianMatrix[row][col] = hungarianMatrix[row][col]
					+ minimumUnmark;
		}

		return findingMostOptimalPathByHungarianAlgorithm(hungarianMatrix);
	}

	public int[][] getMarkPointCoordinator(Float[][] hungarianMatrix) {

		// TODO

		// Find the marked zeros
		int rowLength = hungarianMatrix.length;
		int colLength = hungarianMatrix[0].length;
		float[][] markMatrix = new float[rowLength][colLength];
		float[] rowCheck = new float[rowLength];
		float[] colCheck = new float[colLength];

		for (int row = 0; row < hungarianMatrix.length; row++) {

			for (int col = 0; col < hungarianMatrix[row].length; col++) {

				Float value = hungarianMatrix[row][col];

				if (0 == value && 0 == rowCheck[row] && 0 == colCheck[col]) {

					markMatrix[row][col] = 1; // Mark as selected
					rowCheck[row] = 1;
					colCheck[col] = 1;

				}

			}

		}

		// Find minimum of unmarked
		float minUnmarked =
		for (int row = 0; row < markMatrix.length; row++) {

			for (int col = 0; col < markMatrix[row].length; col++) {
				
				if(markMatrix[row][col]) {
					
				}
				
			}
		}
		// Offset unmarked an get ready for hg again

		return null;

	}

	private List<Integer> fillUpResult(List<Integer> currentResult,
			Float[][] offsetMatrix) {
		if (currentResult.size() == offsetMatrix.length) {
			return currentResult;
		} else {
			List<Integer> result = null;
			Float[] checkingRow = offsetMatrix[currentResult.size()];
			List<Integer> candidates = new ArrayList<Integer>();
			for (int i = 0; i < checkingRow.length; i++) {
				if (checkingRow[i] == 0) {
					boolean colide = false;
					for (Integer alreadyPicked : currentResult) {
						if (alreadyPicked.equals(i)) {
							colide = true;
							break;
						}
					}
					if (!colide) {
						candidates.add(i);
					}
				}
			}
			for (Integer candidate : candidates) {
				List<Integer> possibleResult = new ArrayList<Integer>(
						currentResult);
				possibleResult.add(candidate);
				result = fillUpResult(possibleResult, offsetMatrix);
				if (result != null) {
					return result;
				}

			}
			return result;
		}
	}

	private Float[][] offsetAllRow(Float[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;
		Float[][] offsetMatrix = new Float[rows][cols];
		for (int i = 0; i < rows; i++) {
			float rowMin = Float.MAX_VALUE;

			// Find Smallest value in row
			for (int j = 0; j < cols; j++) {
				if (matrix[i][j] < rowMin) {
					rowMin = matrix[j][i];
				}
			}

			// Offsetting each value in each column
			for (int j = 0; j < cols; j++) {
				offsetMatrix[i][j] = matrix[j][i] - rowMin;
			}
		}
		return offsetMatrix;
	}

	private Float[][] offsetAllCol(Float[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;
		Float[][] offsetMatrix = new Float[rows][cols];
		for (int i = 0; i < cols; i++) {
			float colMin = Float.MAX_VALUE;

			// Find smallest for value in column
			for (int j = 0; j < rows; j++) {
				if (matrix[j][i] < colMin) {
					colMin = matrix[j][i];
				}
			}

			// Offsetting each value in column against smallest value
			for (int j = 0; j < rows; j++) {
				offsetMatrix[j][i] = matrix[j][i] - colMin;
			}
		}
		return offsetMatrix;
	}

	private Float[][] convertToFindingSmallestMatrix(Float[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;
		Float[][] offsetMatrix = new Float[rows][cols];
		for (int i = 0; i < rows; i++) {
			float rowMax = 0;
			for (int j = 0; j < cols; j++) {
				if (matrix[i][j] > rowMax) {
					rowMax = matrix[i][j];
				}
			}
			for (int j = 0; j < cols; j++) {
				offsetMatrix[i][j] = rowMax - matrix[i][j];
			}
		}
		return offsetMatrix;
	}

	Float calculateScoreForOne(String customer, String product) {

		Float score = 0f;

		String c = cleanString(customer);
		String p = cleanString(product);

		if (isEven(p)) {

			score = Float.valueOf(noOfVowels(c) * 1.5f);

		} else {

			score = Float.valueOf(noOfConsonants(c));

		}

		if (gcd(c.length(), p.length()) > 1) {
			score = score * 1.5f;
		}

		return score;
	}

	// Courtesy of
	// http://www.java-tips.org/java-se-tips/java.lang/finding-greatest-common-divisor-recursively.html
	public static long gcd(long a, long b) {
		if (b == 0)
			return a;
		else
			return gcd(b, a % b);
	}

	boolean isEven(String string) {
		return string.length() % 2 == 0;
	}

	int noOfVowels(String product_cleaned) {

		char[] vowels = new char[] { 'a', 'e', 'i', 'o', 'u', 'y', 'A', 'E',
				'I', 'O', 'U', 'Y' };

		char[] letters = product_cleaned.toCharArray();

		int noOfVowels = 0;

		for (int i = 0; i < letters.length; i++) {
			for (int j = 0; j < vowels.length; j++) {
				if (letters[i] == vowels[j]) {
					noOfVowels++;
				}
			}
		}

		return noOfVowels;
	}

	int noOfConsonants(String product_cleaned) {

		String consonantsRegex = "[a-zA-Z&&[^aeiouyAEIOUY]]";

		char[] letters = product_cleaned.toCharArray();

		int noOfConsonants = 0;

		for (int i = 0; i < letters.length; i++) {
			if (String.valueOf(letters[i]).matches(consonantsRegex)) {
				noOfConsonants++;
			}
		}

		return noOfConsonants;
	}

	String cleanString(String string) {
		return string.replaceAll("[ \\-',\\.\\\\\\&0-9]", "");
	}

	Map<String, List<String>> splitRecord(String line) {

		String[] data = null;

		if (line == null) {
			return null;
		}

		data = line.split(";", 2);

		String[] customers = data[0] != null ? data[0].split(",") : null;
		String[] products = data[1] != null ? data[1].split(",") : null;

		Map<String, List<String>> res = new HashMap<String, List<String>>();
		res.put("customers", Arrays.asList(customers));
		res.put("products", Arrays.asList(products));

		return res;

	}
}