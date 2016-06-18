package com.ssj.prototype.prototype;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Runs all unit tests.
@RunWith(Suite.class)
//@Suite.SuiteClasses({GarageTestVIN.class})
@Suite.SuiteClasses({GarageTestVIN.class, GarageTestManual.class})
public class ApplicationTestSuite {
}