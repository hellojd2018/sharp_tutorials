package chap05.cdefaults;

import chap05.defaults.Company;
import chap05.defaults.Employee;

/**
 * 5.6默认方法冲突
 */
public class CompanyEmployee implements Company, Employee {
    private String first;
    private String last;
    //必须重写
    @Override
    public String getName() {
        return String.format("%s working for %s",
            Employee.super.getName(), Company.super.getName());
    }

    @Override
    public void convertCaffeineToCode() {
        System.out.println("Coding...");
    }

    @Override
    public String getFirst() {
        return first;
    }

    @Override
    public String getLast() {
        return last;
    }
}
