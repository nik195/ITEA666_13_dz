package com;

import newThread.NewThread;

public class ApplicationThread {

	public static void main(String[] args) {
		NewThread thread1 = new NewThread("A");
		NewThread thread2 = new NewThread("B");
		thread1.start();
		thread2.start();

		int thread1Sum = 0;
		int thread2Sum = 0;

		while (thread1.isAlive() || thread2.isAlive()) {

		}

		for (int a : thread1.getSumArray()) {
			thread1Sum += a;
		}

		System.out.println(thread1.geTName() + " sum: " + thread1Sum);

		for (int a : thread2.getSumArray()) {
			thread2Sum += a;
		}

		System.out.println(thread2.geTName() + " sum: " + thread2Sum);

		if (thread1Sum > thread2Sum) {
			System.out.println(thread1.geTName() + " win");
		} else if (thread2Sum > thread1Sum) {
			System.out.println(thread2.geTName() + " win");
		} else {
			System.out.println("Draw");
		}

	}

}
