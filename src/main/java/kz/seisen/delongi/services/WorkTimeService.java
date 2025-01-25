package kz.seisen.delongi.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
public class WorkTimeService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final LocalTime START_TIME = LocalTime.of(8, 0);
    private static final LocalTime END_TIME = LocalTime.of(17, 0);
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Almaty");


    @Cacheable("holidays")
    public List<LocalDate> getHolidays(String countryCode) {
        String url = "https://date.nager.at/api/v3/publicholidays/" + LocalDate.now().getYear() + "/" + countryCode;
        var response = restTemplate.getForObject(url, PublicHoliday[].class);

        return response == null ? List.of() :
                Arrays.stream(response).map(PublicHoliday::getDate).toList();
    }

    public boolean isWithinWorkingHours() {

        LocalTime now = LocalTime.now(DEFAULT_ZONE).withNano(0);
        DayOfWeek today = LocalDate.now(DEFAULT_ZONE).getDayOfWeek();

        System.out.println("Today: " + today);

        return now.isAfter(START_TIME) && now.isBefore(END_TIME)
                && today != DayOfWeek.SATURDAY
                && today != DayOfWeek.SUNDAY;
    }

    public boolean isHoliday(List<LocalDate> holidays) {
        return holidays.contains(LocalDate.now(DEFAULT_ZONE));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private static class PublicHoliday {
        private LocalDate date;

        public LocalDate getDate() {
            return date;
        }
    }
}
