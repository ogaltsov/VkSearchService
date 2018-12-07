package ru.krotov.teenssearchservice.configurations.filters.user;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.configurations.filters.Filter;

import java.time.LocalDate;
import java.time.Period;

@Component
public class AgeUserFilter extends AbstractUserFilter {

	private static final int MIN_AGE = 18;

	public AgeUserFilter(@Qualifier("userFilterExecutor") Filter<UserXtrCounters> filter) {
		super(filter);
	}

	// TODO: Дубликат UserDto
	@Override
	public boolean filter(UserXtrCounters userXtrCounters) {

		String bDay = userXtrCounters.getBdate();
		if (StringUtils.isEmpty(bDay)) {
			return true;
		}

		String[] split = bDay.split("\\.");

		int monthOfBirthDay = Integer.parseInt(split[1]);
		int dayOfBirthDay = Integer.parseInt(split[0]);
		if (split.length < 3) {
			return true;
		}

		int yearOfBirthDay = Integer.parseInt(split[2]);
		int age = calculateAge(yearOfBirthDay, monthOfBirthDay, dayOfBirthDay);

		return age >= MIN_AGE;
	}

	private int calculateAge(int year, int month, int day) {

		LocalDate birthDay = LocalDate.of(year, month, day);
		LocalDate now = LocalDate.now();

		return Period.between(birthDay, now).getYears();
	}
}
