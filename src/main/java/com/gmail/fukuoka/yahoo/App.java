package com.gmail.fukuoka.yahoo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public final class App {
    private App() {
	}
	
    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
		int size = 9;
		ExecutorService exec = Executors.newFixedThreadPool(size);
		List<Integer> indexList = new ArrayList<Integer>();
		for (int i = 1; i <= size; i++) {
			indexList.add(i);
		}
		List<CompletableFuture<Integer>> futureList = indexList.stream()
				.map(i -> (CompletableFuture<Integer>) CompletableFuture.supplyAsync(() -> shori(i), exec)) //execは任意で
				.collect(Collectors.toList());

		// Listを配列に変換し、CompletableFutureのStaticメソッドallOfに入れて、joinします。
		// 全てのスレッドが終了するまで待機します。
		CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();
		
		// futurListからCompletableFutureを取り出して、結果を取得して、標準出力に表示します。
		futureList.forEach(result -> {
			try {
				System.out.println(result.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
        });    
	}
	/**
	 * 非同期で処理したいやつ
	 * @param para
	 * @return
	 */
    private static int shori(int para) {
		return para;
	}
}
