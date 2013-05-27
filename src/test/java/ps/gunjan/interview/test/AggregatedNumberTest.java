package ps.gunjan.interview.test;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ps.gunjan.interview.util.AggregatedNumber;

//@RunWith(Arquillian.class)
public class AggregatedNumberTest {

	@Inject
	AggregatedNumber aggNum;

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class, "interview.jar")
				.addPackage(AggregatedNumber.class.getPackage());

	}

	@Test
	public void is_aggregated_number_112358() {
		Assert.assertEquals("match", AggregatedNumber.check(112358));
	}

	@Test
	public void is_aggregated_number_1299111210() {
		Assert.assertEquals("match", AggregatedNumber.check(1299111210));
	}

	@Test
	public void is_aggregated_number_112112224() {
		Assert.assertEquals("match", AggregatedNumber.check(112112224));
	}
	
	@Test
	public void is_not_aggregated_number_11111111() {
		Assert.assertEquals("mis-match", AggregatedNumber.check(11111111));
	}
	
	@Test
	public void is_single_digit_number() {
		Assert.assertEquals("match", AggregatedNumber.check(1));
	}

}
