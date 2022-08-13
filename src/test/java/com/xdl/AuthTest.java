package com.xdl;

import com.xdl.bean.Author;
import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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
                .map(author -> author.getAge() + 10)
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

    /**
     * 终结操作，只有执行了终结操作才会启动对流的操作
     */
    @Test
    public void test04() {
        // forEach 对流中的元素进行遍历操作，我们通过传入的参数去指定对遍历到的元素进行什么具体操作
        List<Author> authors = Author.getAuthors();
        authors.stream()
                .map(author -> author.getName())
                .distinct()
                .forEach(name -> System.out.println(name));
        // count 用来获取当前流中元素的个数
        System.out.println("-----------count-----------");
        long count = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .count();
        System.out.println(count);
        // max&min可以用来获取流中的最大值和最小值
        System.out.println("-----------max&min-----------");
        Optional<Integer> max = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .map(book -> book.getScore())
                .max((o1, o2) -> o1 - o2);
        System.out.println("max:" + max.get());
        Optional<Integer> min = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .map(book -> book.getScore())
                .min((o1, o2) -> o1 - o2);
        System.out.println("min:" + min.get());
        // collect 把当前流转换成一个集合
        System.out.println("-----------collect-----------");
        List<String> collect = authors.stream()
                .map(author -> author.getName())
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 终结操作，查找与匹配
     */
    @Test
    public void test05() {
        List<Author> authors = Author.getAuthors();
        // anMatch可以用来判断是否有任意符合匹配条件的元素，结果为boolean类型
        // 判断是否有年龄超过29的作家
        boolean flag = authors.stream()
                .anyMatch(author -> author.getAge() > 29);
        System.out.println(flag);
        // allMatch用来判断是否所有元素都满足条件
        boolean flag2 = authors.stream()
                .allMatch(author -> author.getAge() > 29);
        System.out.println(flag2);
        // noneMatch用来判断是都所有元素都不满足条件
        boolean flag3 = authors.stream()
                .noneMatch(author -> author.getAge() > 100);
        System.out.println(flag3);
        // findAny获取流中任意元素，该方法没办法保证获取的一定是流中的第一个元素
        // 获取任意一个年龄大于18的作家，如果存在就输出名字
        Optional<Author> optional = authors.stream()
                .filter(author -> author.getAge() > 18)
                .findAny();
        System.out.println(optional.get().getName());
        // findFirst获取流中第一个元素
        Optional<Author> first = authors.stream()
                .sorted((o1, o2) -> o1.getAge() - o2.getAge())
                .findFirst();
        first.ifPresent(author -> System.out.println(author.getName()));
        // reduce 对流中的数据按照指定的计算方式计算出结果。（缩减操作）
        //  reduce的作用是把stream中的元素给组合起来，
        //  我们可以传入一个初始值，它会按照我们的计算方式依次拿流中的元素和初始化值进行计算，计算结果再和后面的元素计算。
        Integer total = authors.stream()
                .distinct()
                .map(author -> author.getAge())
                .reduce(0, (result, ele) -> result + ele);
        System.out.println(total);
        // map一个参数的重载形式
        Optional<Integer> total2 = authors.stream()
                .distinct()
                .map(author -> author.getAge())
                .reduce((result, ele) -> result + ele);
        System.out.println(total2.get());
        // 使用reduce找出流中的最值
        // 找出年纪最大的作者
        Integer result = authors.stream()
                .map(author -> author.getAge())
                .reduce(Integer.MIN_VALUE, (res, ele) -> res > ele ? res : ele);
        System.out.println(result);

    }
}
