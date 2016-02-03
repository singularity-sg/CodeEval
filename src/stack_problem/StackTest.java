package stack_problem;

public class StackTest {

	public static void main(String... s) {
		MyStack stack = new MyStack();

		stack.push(10);
		stack.push(20);
		stack.push(30);
		stack.push(40);
		stack.push(50);
		stack.push(60);

		assert getStackElement(stack, 0) == 60;
		assert getStackElement(stack, 2) == 40;
		assert getStackElement(stack, 5) == 10;
		try {
			getStackElement(stack, 6);
			assert false;
		} catch (Exception e) {
			assert true;
		}

		assert getStackElement(stack, 0) == 60;
	}

	public static int getStackElement(MyStack stack, int index) {
		int top = 0;
		int answer = 0;
		
		try {
			top = stack.pop();
			if(index == 0) {
				answer = top;
			} else {
				if(!stack.isEmpty()) {
					answer = getStackElement(stack, --index);
				} else {
					throw new RuntimeException("My own exception");
				}
			}
		} finally {
			stack.push(top);
		}
		
		return answer;
	}

}
