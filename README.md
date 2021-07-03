# Spring-5-Learning

**Spring 的2大核心思想: IOC/DI(控制反转) 以及 AOP(面向切面编程)**

[TOC]



## IOC/DI 

在传统的编程当中,如果一个类A 依赖于另一个类B， 我们可以在A类中new一个B类。但是这样做有个坏处，类和类之间的耦合度太高，不利于扩展以及维护。不符合高内聚低耦合的设计思想。下面用一个小案例进行解释:

假设一位公司员工每天开车去上班，以下是耦合度很高的情况。

```java
/**
 * 一位公司员工每天开车去上班
 */
public class Person {

    private Car car=new Car();
    
    public void goToWork(){
        car.take();
    }

}
```

```java
public class Car {

    public void take(){
        System.out.println("开车去上班。");
    }
}
```

如果需求改变，公司员工做班车去上班，我们需要做以下修改:

```java
public class Bus {
    
    public void take(){
        System.out.println("坐班车去上班");
    }
}
```

```java
/**
 * 一位公司员工每天开车去上班
 */
public class Person {
		//这里修改成ShuttleBus
    private ShuttleBus bus=new ShuttleBus();

    public void goToWork(){
      	//修改成bus调用take方法
        bus.take();
    }

}
```

如果每次都是这么修改,在现实开发中类和类分复杂关系远比案例中的类和类之间关系复杂,这样就造成不利于维护以及扩展。如果Person类依赖的不是实体类，而是抽象，并且通过构造方法或者set方法传递引用，这样可以减少类和类之间的耦合。代码如下

```java
/**
 * 交通工具接口
 */
public interface Vehicle {
    
    public void take();
}
```

```java
/**
 * 实现Vehicle接口并实现take方法
 */
public class Car implements Vehicle {

    public void take(){
        System.out.println("开车去上班。");
    }
}
```

```java
/**
 * 实现Vehicle接口，并实现take方法
 */
public class ShuttleBus implements Vehicle {

    public void take(){
        System.out.println("坐班车去上班");
    }
}
```

```java
/**
 * 一位公司员工每天开车去上班
 */
public class Person {

    private Vehicle vehicle;
    
    public Person(){}
    public Person(Vehicle vehicle){
        this.vehicle=vehicle;
    }

    public void goToWork(){
        vehicle.take();
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
```

相对于原来的代码，这样的设计可以减少代码修改, 我们只需要把Vehicle实现类创建好并通过构造方法或者set方法传递即可。

```java
public class Test {

    public static void main(String[] args) {
        //创建一个ShuttleBus对象
        ShuttleBus bus=new ShuttleBus();
        //创建一个Person对象
        Person person=new Person();
        //也可以通过构造方法传递
        //Person person=new Person(bus);
        person.setVehicle(bus);
        person.goToWork();
    }
}
```

这样方式虽然耦合度相比原来的代码确实降低了，因为Person类中的代码无需再修改。可是这样就出现了新的问题，在main方法中，我们还是使用了new关键字进行创建对象。然后把bus对象通过构造方法或者set方法传入到Person类中。

解决方法:

​	**Spring通过IOC(控制反转)/DI(依赖注入) 帮我们解决此问题。我们知道，创建对象方式不止new关键字这一种，我们还可以通过反射技术创建对象。Spring底层通过反射的技术帮我们创建对象，并且存入到容器中。该容器是HashMap。对于开发者来说，只需要在xml配置文件中配置一些对象信息即可。除了创建对象以外，spring也可以实现依赖注入: 在上面的例子中,人和交通工具通过构造方法或者set方法实现依赖关系。**



## Spring入门之创建对象

### Spring入门之普通方法创建对象

1.创建一个maven工程

2.在pom.xml文件中导入一些需要的jar包

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-context</artifactId>
  <version>5.2.9.RELEASE</version>
</dependency>
<dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
</dependency>
```

3.创建一个Person类

```java
public class Person {
    private String name;
    private Integer age;

    public Person(){}
    public Person(String name,Integer age){
        this.name=name;
        this.age=age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

4.在resources资源包下创建一个xml配置文件，xml配置文件名字可以自己取。这里我会取名:

applicationContext.xml

5.往applicationContext.xml里导入spring的约束，可以从spring官网中查找:

https://docs.spring.io/spring-framework/docs/current/reference/html/core.html

6.在xml配置文件中，我们可以将需要用到的对象写在该配置文件中。目前就一个Person对象。

在该配置文件中，我们需要一个bean标签用来配置对象信息，其中有2个重要的属性。

id: 给该对象(bean)取一个名字，后续可以通过id来获取对象

class:全类名

**有了这两个关键属性后，当spring解析xml配置文件时，会把id作为key存入map中，由于有了全类名，spring可以使用反射技术创建对象并存入到key对应的value中。**

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="person" class="com.java.domain.Person"/>
</beans>
```

注:目前仅仅是通过spring帮我们创建对象。所以约束仅仅需要这些就足够。如果后续使用spring的其他功能，那么则需要相应的约束。

7.创建一个Test类，使用spring创建对象。

```java
public class TestSpring {

    public static void main(String[] args) {
        //1.实例化一个ApplicationContext,在创建applicationContext实例对象时，我们需要把
      	//配置文件的名称通过构造方法形式传递。
        ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
      	/**
      	两种方式获取对象：
      		1.只需要bean 的id 返回的是object对象，需要转型
      		2.bean的id+该类的class类 这样返回对象不需要转型
      	**/
        //Person person =(Person)ac.getBean("person");
        Person person=ac.getBean("person",Person.class);
    }
}
```

## Spring创建对象之通过工厂创建对象

我们可以通过示例工厂来创建对象

1.创建一个Aniaml类

```java
public class Animal {
    private String animalName;

    public Animal(){}
    public Animal(String animalName){
        this.animalName=animalName;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }
}
```

2.创建一个BeanFactory

```java
public class BeanFactory {


    public Animal getAnimal(){
        Animal animal=new Animal();
        animal.setAnimalName("dog");
        return animal;
    }
}
```

3.配置xml

因为BeanFactory也是一个对象，因此先配置好BeanFacotory对象

因为Animal是通过工厂来创建的，那么我们需要用到factory-bean以及factory-method属性。

factory-bean:传入一个刚刚配置好的beanFactory即可

factory-method:将工厂方法传入，我们这里是getAnimal()方法

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    
    <bean id="beanFactory" class="com.java.beanfactory.BeanFactory"/>
    <bean id="animal" factory-bean="beanFactory" factory-method="getAnimal"/>
</beans>
```

## Spring创建对象之静态工厂创建对象

该方法和工厂方法的区别就在于不需要创建工厂对象。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="animal02" class="com.java.beanfactory.BeanStaticFactory" factory-method="getAnimal"/>
</beans>
```

## Spring之依赖注入(DI)

之前我们我们说过，类与类之间的依赖关系，可以直接在一个类中直接new一个类，那么这样的方式耦合很高。或者，我们可以通过构造方法或者set方法进行注入。Spring也为我们提供2中依赖注入:构造方法注入以及set方法注入。

### Spring依赖注入之构造

### 方法注入

**假设我们要给Person注入一些属性。**

```java
public class Person {
    private String name;
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

1.首先在applicationContext.xml中创建一个bean 对象。这个和之前通过bean创建对象没有区别。

2.在bean标签中 我们需要写一个<constructor-arg name="" value=""/>

**constructor-arg标签中有几个重要的属性:**

**name:用于指定给构造方法中指定名称参数赋值。**

**Index:表示构造函数中参数的位置，从0开始**

**type:指定要注入的数据的数据类型**

**value:用于提供基本数据类型和string类型数据**

**ref:用于指定bean类型的数据，它指定的是spring的IOC 核心容器中出现过的bean对象**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
		<!--在Person类的构造方法中，传递的参数是String name 以及int age
 				因此我们可以通过 name属性来选择参数，value参数来赋值
				注:这里的value参数只能写基本数据类型或者String
		-->
    <bean id="person" class="com.java.domain.Person">
        <constructor-arg name="name" value="kevin"/>
        <constructor-arg name="age" value="20"/>
    </bean>
</beans>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

  	<!--由于index是指参数的位置，从0开始，那么可读性比较差，因为我们不知道这个index
 				对应的数据类型是什么，因此我们可以用index+type 这两个属性一起使用。
				一般来说，使用 name+value这一组比较方便。
				注:如果是基本数据类型，type需要些包装类的全类名，否则会报异常。
		-->
    <bean id="person" class="com.java.domain.Person">
        <constructor-arg index="0" type="java.lang.String" value="kevin"/>
        <constructor-arg index="1" type="java.lang.Integer" value="20"/>
    </bean>
</beans>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
		
  	<!--员工做班车去上班，员工依赖于班车
 				因此我们需要把班车这个对象注入到员工对象中。
				1.首先我们先配置ShuttleBus对象
				2.通过<constructor-arg>标签实现注入。
				这里我们使用ref 引用来注入ShuttleBus。
				ref引用就是bean的id值，找到对应的id值即可实现注入
		-->
    <bean id="vehicle" class="com.java.vehicle.ShuttleBus"/>
    <bean id="employee" class="com.java.vehicle.Employee">
        <constructor-arg name="vehicle" ref="vehicle"/>
    </bean>
</beans>
```

### Spring依赖注入之set方法注入:

通过set方法注入，我们需要用到<property>标签。该标签只有3个属性:

**name, ref, value**，意思和<constrcutor-arg>标签中的**name ref value**一样

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
		<!--如果想通过set方法注入，person类中得有一个空的构造方法-->
    <bean id="person" class="com.java.domain.Person">
        <property name="name" value="kevin"/>
        <property name="age" value="20"/>
    </bean>
</beans>
```

### Spring注入集合数据

一个类中会有集合数据: List, set, map, array等。通过set方法注入集合数据。

**注:在配置过程中, List set array的标签可以互用。**

​	 **map和prop的标签可以互用，因为map和prop都属于key-value形式**

```java
package com.java.domain;

import java.util.*;

public class Person {
    private String name;
    private Integer age;
    private String[] myArrays;
    private List<String> list;
    private Set<String> set;
    private Map<String,String> map;
    private Properties pro;

    public Person(){}
    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setMyArrays(String[] myArrays) {
        this.myArrays = myArrays;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void setPro(Properties pro) {
        this.pro = pro;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", myArrays=" + Arrays.toString(myArrays) +
                ", list=" + list +
                ", set=" + set +
                ", map=" + map +
                ", pro=" + pro +
                '}';
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="person" class="com.java.domain.Person">
        <property name="name" value="kevin"/>
        <property name="age" value="20"/>
        <property name="myArrays">
            <array>
                <value>A</value>
                <value>B</value>
                <value>C</value>
            </array>
        </property>
        <property name="list" >
            <list>
                <value>AA</value>
                <value>BB</value>
                <value>CC</value>
            </list>
        </property>
        <property name="set">
            <set>
                <value>A</value>
                <value>E</value>
            </set>
        </property>
        <property name="map">
            <map>
                <entry key="1" value="kevin"/>
                <entry key="2" value="Bob"/>
            </map>
        </property>
        <property name="pro">
            <props>
                <prop key="4">David</prop>
                <prop key="5">James</prop>
            </props>
        </property>
	</bean>
</beans>
```

## Spring中bean对象的研究

1.我们在xml配置文件中可以配置任何对象，那么这些对象会被存入到spring的IOC容器中。那么这些对象到底是什么时候被创建好了？

我们在Person类的构造方法中打印一句话:

```java
public class Person {
    private String name;
    private Integer age;
    private String[] myArrays;
    private List<String> list;
    private Set<String> set;
    private Map<String,String> map;
    private Properties pro;

    public Person(){
        System.out.println("Person对象被创建好了...");
    }
  	//set 和 get 方法....
```

我们想要获取Person对象:

1.先创建SpringIOC的容器 

```java
ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
```

2.通过getBean方法来获取对象:

```java
Person person = ac.getBean("person", Person.class);
```

假设我们只创建SpringIOC容器，并不调用getBean方法，然后执行，我们发现在控制台中还是会输出Person对象被创建好了...。由此我们可以证明Spring是默认在创建IOC时候，就顺便把bean对象创建好并存进IOC容器。那么该对象的生命周期和IOC是相同的，只要容器在，那么该对象就会一直在。因此，默认情况下，bean对象是单例的。

**两次获取person对象，控制台中只会输出一次Person对象被创建好了...。 并且使用==号来判断是否同一个对象，结果为true。**

```java
public static void main(String[] args) {
    ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
    Person person1 = ac.getBean("person", Person.class);
    Person person2=ac.getBean("person",Person.class);
    System.out.println(person1==person2);//true

}
```

我们可以通过bean标签中的scope属性来设置对象创建的时机。

Scope: 

1.singleton 单例

2.prototype 多例

如果不添加scope属性，那么默认是单例的。

```xml
<!--如果是多例的，那么在getBean方法获取对象时才创建 -->
<bean id="person" class="com.java.domain.Person" scope="protoype" >
    <property name="name" value="kevin"/>
    <property name="age" value="20"/>
</bean>
```

## Spring之注解

我们除了使用xml配置文件来创建对象和注入对象以外，我们还可以使用注解的方式实现创建对象和注入对象。

### @Component

@Component: 用于把当前类对象存入spring容器中。属性：value。用于指定bean的id，当然可以不写，默认是当前类名的且首字母小写。

1.因为要使用注解开发，那么我们需要从spring官网中找到注解的约束并粘贴到applicationContext.xml中

2.创建一个类，并在类上写@Component

3.在applicationConetxt.xml配置文件中添加component-scan标签，该标签可以告知spring在创建容器时，如果有@Component，那么就创建该对象并存入IOC容器中.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd">
        
    <context:component-scan base-package="com.java"/>
    <context:annotation-config/>
    
</beans>
```

```java
ApplicationContext ac=new AnnotationConfigApplicationContext("com.java");
Person person = ac.getBean("person", Person.class);
```

4.通过new AnnotationConfigApplicationContext来创建容器，并通过getBean方法获取对象。

由于web 开发可以分成3层架构，web表现层，service业务逻辑层，dao持久层。

那么@Component可以细分为3中

@Controller它对应的是表现层

@Service它对应的是业务逻辑层

@Repository它对应的是dao层

@Controller @Service @Repository 这三个功能和@Component功能一模一样,只是因为web3层架构所以才会细分。

### @Autowired

该注解的作用是自动按照类型注入。该注解可以作用在变量，方法上。

我们知道Spring的IOC容器是HashMap，Key是String类型的，Value是Object类型的。

**如果容器中只存在唯一的类型，那么只要注入的数据类型和容器中的类型匹配就可以成功自动注入。**

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
}
```

| Key：String类    | value：Object类        |
| ---------------- | ---------------------- |
| "accountDao"     | AccountDaoImpl对象     |
| "accountService" | AccountServiceimpl对象 |
|                  |                        |

**如果容器中存在多个相同的类型，那么需要通过变量名来判断。如果找到则成功自动注入。**

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao1;
}
```

| Key: String类    | Value:Object类         |
| ---------------- | ---------------------- |
| "accountDao1"    | AccountDaoImpl对象     |
| "accountDao2"    | AccountDaoImpl对象     |
| "accountService" | AccountServiceimpl对象 |

### @Qualifier

**如果容器中存在多个相同的类型，但是变量名却不匹配，如下所示。accountDao变量名和key中的任何一个值不匹配，找不到对象。那么解决该方法就是@Autowired和@Qualifier一起使用**

**因此@Qualifier的作用就是在按照勒种注入的基础上，再按照名称注入，他在给类成员注入时必须和@Autowired一起使用。@Qualifier可以单独使用，但是只能作用在方法时，后续讲解使用注解来替换applicationContext.xml配置时再详细讲解。**

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
}
```

| Key: String类    | Value:Object类         |
| ---------------- | ---------------------- |
| "accountDao1"    | AccountDaoImpl对象     |
| "accountDao2"    | AccountDaoImpl对象     |
| "accountService" | AccountServiceimpl对象 |

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    @Qualifier(value = "accountDao1")
    private AccountDao accountDao;
}
```

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    @Qualifier(value = "accountDao1")
    private AccountDao accountDao;
}
```

**@Autowired @Qualifier 不能给String类型和基本数据类型注入。**

### @Value

作用: 用于注入基本数据类型和String类型数据

属性:value 赋值。 他可以使用Spring spEL表达式 --> ${表达式}

```java
@Component
public class Person {
    @Value("kevin")
    private String name;
  	//SPEL表达式
    @Value("${男}")
    private String sex;
    @Value("20")
    private int age;

    public Person(){
        System.out.println("Person对象被创建了");
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }
}
```

### @Scope

作用:用于指定bean的作用范围。

属性 value: singleton(单例)/prototype(多例)

和在xml配置文件中bean标签配置scope属性是一模一样。

