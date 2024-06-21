package com.ayb;

import com.ayb.entity.Show;
import com.ayb.entity.ToEmail;
import com.ayb.service.ShowService;
import com.ayb.service.impl.ShowServiceImpl;
import com.ayb.uitls.SysConstants;
import com.ayb.uitls.UserConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ayb.uitls.UserConstants.SHOW_GEO_KEY;

@SpringBootTest
class ShowTypeLiveApplicationTests {

    @Resource
    JavaMailSender mailSender;

    @Resource
    private ShowServiceImpl showService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
//        List<Show> list = showService.list();
        System.out.println("123");
        List<Show> shows = showService.list();
        System.out.println(shows);
    }

    @Test
    void loadShowData() {
        //search
        List<Show> list = showService.list();
        //classify by id
        Map<Integer, List<Show>> map = list.stream().collect(Collectors.groupingBy(Show::getType));
        for (Map.Entry<Integer, List<Show>> entry : map.entrySet()) {
            Integer typeId = entry.getKey();
            List<Show> Shows = entry.getValue();
            String key = UserConstants.SHOW_GEO_KEY + typeId;
            List<RedisGeoCommands.GeoLocation<String>> locations = new ArrayList<>(Shows.size());
            for (Show show : Shows) {
                locations.add(new RedisGeoCommands.GeoLocation<>(
                                show.getShowId().toString(), new Point(show.getX(), show.getY())
                        )
                );
            }
            stringRedisTemplate.opsForGeo().add(key, locations);
        }
    }


    @Value("${spring.mail.username}")
    private String from;

    @Test
    void testSendMail() {
        ToEmail toEmail = new ToEmail();
        //创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom(from);
        //谁要接收
        message.setTo("sylybimike@163.com");
        //邮件标题
        message.setSubject("WHO ARE YOU");
        //邮件内容
        message.setText("普通朋友");
        mailSender.send(message);
    }
}
