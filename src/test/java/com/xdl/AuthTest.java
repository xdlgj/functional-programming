package com.xdl;

import com.xdl.bean.Author;
import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class AuthTest {

    /**
     * 获取所有年龄小于18的作家的名字
     */
    @Test
    public void test01() {
        List<Author> authors = Author.getAuthors();
        authors
                .stream() // 把list集合转换成流
                .distinct() // 先去除重复的作家
                .filter(author -> author.getAge() < 18) // 筛选年龄小于18岁的作家
                .forEach(author -> System.out.println(author.getName())); // 编译打印名称
    }

    /**
     * 创建流的操作
     * 1、单列集合：集合对象.stream()
     * 2、数组:    Arrays.stream(数组) 或 Stream.of(数组)
     * 3、双列集合：转换成单列集合后再创建
     */
    @Test
    public void test02() {
        // 单列集合
        List<Author> authors = Author.getAuthors();
        Stream<Author> stream = authors.stream();
        // 数组
        Integer[] arr = {1, 2, 3, 4, 5};
        Stream<Integer> stream1 = Arrays.stream(arr);
        Stream<Integer> stream2 = Stream.of(arr);
        // 双列集合，map
        HashMap<String, Integer> map = new HashMap<>();
        map.put("aa", 11);
        map.put("bb", 22);
        map.put("cc", 33);
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println(entry); // aa=11 bb=22 cc=33
        }
        Stream<Map.Entry<String, Integer>> stream3 = entries.stream();
    }

    /**
     * 中间操作
     */
    @Test
    public void test03() {
        // filter 可以对流中的元素进行过滤，符合过滤条件的才能继续保留在流中。
        List<Author> authors = Author.getAuthors();
        authors.stream()
                .filter(author -> author.getName().length() > 1)
                .forEach(author -> System.out.println(author.getName()));
        // map可以把对流中的元素进行计算或转换
        System.out.println("--------------map---------------");
        authors.stream()
                .map(author -> author.getName())
                .forEach(name -> System.out.println(name));
        authors.stream()
                .map(author -> author.getAge()+10)
                .forEach(age -> System.out.println(age));
        // distinct 去除流中重复的元素, 依赖与Object的equals方法来判断是否是相同的对象
        System.out.println("--------------distinct---------------");
        authors.stream()
                .distinct()
                .forEach(author -> System.out.println(author));
        // sorted 对流中的元素进行排序
        System.out.println("--------------sorted---------------");
        authors.stream()
                .distinct()
                .sorted() // com.xdl.bean.Author cannot be cast to java.lang.Comparable, 需要对象类实现Comparable
                .forEach(author -> System.out.println(author.getAge()));
        System.out.println("--------------sorted带参数---------------");
        authors.stream()
                .distinct()
                .sorted((o1, o2) -> o1.getAge() - o2.getAge())
                .forEach(author -> System.out.println(author.getAge()));
        // limit 设置流的最大长度，超出的部分将被抛弃
        System.out.println("-------------limit---------------");
        authors.stream()
                .distinct()
                .sorted()
                .limit(2)
                .forEach(author -> System.out.println(author.getName()));
        // skip 跳过流中的前n个元素，返回剩下的元素
        System.out.println("-------------skip---------------");
        authors.stream()
                .distinct()
                .sorted()
                .skip(1)
                .limit(1)
                .forEach(author -> System.out.println(author.getName()));
        // flatMap map只能把一个对象转换成另一个对象来作为流中的元素。而flatMap可以把一个对象转换成多个对象作为流中的元素
        System.out.println("-------------flatMap---------------");
        authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .forEach(book -> System.out.println(book.getName()));
        // 打印现有数据的所有分类，要求对分类进行去重，不能出现 ,逗号分割的情况
        authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .flatMap(book -> Arrays.stream(book.getCategory().split(",")))
                .distinct()
                .forEach(category -> System.out.println(category));
    }
}
