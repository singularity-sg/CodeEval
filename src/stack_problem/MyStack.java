package stack_problem;

class MyStack {
	
	private java.util.List<Integer> items;

	public MyStack() {
		items = new java.util.ArrayList<Integer>();
	}

	public int pop() {
		if (isEmpty())
			throw new RuntimeException("Stack is empty!");
		return items.remove(items.size() - 1);
	}

	public void push(Integer i) {
		items.add(i);
	}

	public int peek() {
		if (isEmpty())
			throw new RuntimeException("Stack is empty!");
		return items.get(items.size() - 1);
	}

	public boolean isEmpty() {
		return items.size() == 0;
	}
	
}
