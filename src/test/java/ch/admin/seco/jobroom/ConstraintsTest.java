package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.JobOfferDatasetHelper;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.json.JsonObject;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
public class ConstraintsTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ApiTestHelper apiTestHelper;

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        restAccessKeyRepository.save(apiTestHelper.getDefaultRestAccessKey());
    }

    @After
    public void cleanup() {
        apiTestHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        apiTestHelper.unAuthenticate();
        restAccessKeyRepository.deleteAll();
    }

    @Test
    public void sizeJobDescription() throws Exception {

        String jobDescription = getLongJobDescription();
        Assert.assertEquals(10001, jobDescription.length());

        JsonObject jobOffer = JobOfferDatasetHelper.getJsonWithJobDescription(jobDescription);

        this.mockMvc.perform(post("/joboffers").with(apiTestHelper.getDefaultHttpBasic())
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(jobOffer.toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * @return 10001 characters text
     */
    private String getLongJobDescription() {

        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse at nibh et" +
                " libero vulputate eleifend vitae vel nisl. Ut convallis vehicula mi venenatis congue. Vestibulum" +
                " quis fermentum purus, id semper tortor. Integer ut felis eros. Nunc hendrerit porta purus in" +
                " pretium. Proin metus lorem, posuere vitae faucibus sed, sollicitudin in felis. Aliquam tempor" +
                " commodo leo, quis dapibus ex euismod eu.Class aptent taciti sociosqu ad litora torquent per" +
                " conubia nostra, per inceptos himenaeos. Duis eget purus id ipsum lacinia posuere sit amet a" +
                " turpis. Sed eu lacus erat. Nam non nunc eu metus faucibus malesuada. Proin ultricies pulvinar" +
                " ligula quis scelerisque. Fusce interdum tortor et eros tempus feugiat. Morbi non efficitur risus." +
                " Quisque sodales arcu eget maximus porta. Vestibulum sed facilisis massa, vitae consequat libero." +
                " Mauris elementum, mi sit amet rhoncus posuere, nunc orci semper tellus, lobortis dignissim leo" +
                " metus dictum urna. Curabitur tristique orci a mauris commodo suscipit. Sed velit lacus, gravida" +
                " vitae vulputate vitae, varius ut erat. Morbi tempus velit ac ipsum dapibus, at ultrices leo" +
                " egestas. Curabitur interdum, massa eget tincidunt gravida, eros tellus dictum magna, id ultricies" +
                " erat tellus accumsan neque. In sit amet purus sed arcu commodo cursus.Duis lobortis turpis" +
                " faucibus mauris rutrum tincidunt. Vestibulum quis sem ut dolor vulputate dapibus. Pellentesque" +
                " in enim lectus. Quisque ultrices lorem purus, ac semper orci pellentesque non. Integer eget mollis" +
                " ipsum. Etiam imperdiet dui vel lacus finibus, vel dictum ante finibus. Vivamus id facilisis magna," +
                " non pharetra lorem. Integer elementum pretium ipsum eu pharetra. Morbi bibendum imperdiet" +
                " luctus.Fusce malesuada ex lorem, in hendrerit ipsum elementum sollicitudin. Interdum et malesuada" +
                " fames ac ante ipsum primis in faucibus. Praesent consequat, mi ac malesuada ornare, ex arcu" +
                " tempus eros, in luctus felis ipsum a ex. Class aptent taciti sociosqu ad litora torquent per" +
                " conubia nostra, per inceptos himenaeos. Phasellus condimentum felis a maximus blandit. Quisque" +
                " accumsan lacus nulla, varius iaculis lacus porta at. Nullam eget dolor ipsum. Donec at efficitur" +
                " ex, sed maximus orci. Donec et ipsum ut est cursus tempus. Phasellus tempor sit amet dolor non" +
                " varius.Sed sit amet est et neque pulvinar bibendum finibus sit amet nisi. Nam urna purus, commodo" +
                " eget pharetra a, lobortis non enim. Fusce venenatis ex vel efficitur tincidunt. Suspendisse" +
                " luctus auctor lorem, lacinia iaculis dui fermentum quis. Maecenas mattis nunc vel dolor placerat" +
                " tincidunt. Duis non libero quis sem suscipit viverra. Fusce at purus eu dui consectetur feugiat." +
                " Vestibulum ultricies risus lacus, sit amet ornare diam feugiat vel. Mauris id feugiat arcu." +
                " Vestibulum vulputate tincidunt mauris et pellentesque. Vivamus eu pretium velit, quis commodo" +
                " nisl. Phasellus felis quam, commodo non dolor vitae, commodo auctor mauris. Quisque eu erat vel" +
                " purus euismod laoreet non sed nisi.Vestibulum tempus semper libero, vel feugiat eros pellentesque" +
                " scelerisque. Phasellus tristique, lorem ac facilisis viverra, erat tellus pretium velit, eget" +
                " feugiat mi lectus et leo. Vivamus efficitur risus tincidunt leo fermentum, vel fermentum orci" +
                " lobortis. Praesent aliquet quam quis libero tincidunt rhoncus. Nullam lobortis turpis nec mattis" +
                " pulvinar. Quisque odio urna, mattis ut tempor sed, auctor sed leo. Vivamus faucibus maximus" +
                " sapien, et efficitur diam tincidunt in. Cras libero enim, varius sed erat vel, viverra dictum" +
                " est. In dapibus nibh imperdiet, dignissim ligula eget, convallis leo. Donec luctus turpis ut massa" +
                " varius, vitae ullamcorper velit vestibulum. Maecenas pharetra leo non eros faucibus laoreet." +
                " Sed placerat odio at felis lacinia iaculis. Mauris libero tellus, aliquet vitae lorem non," +
                " feugiat porttitor quam. Nunc id sapien id lacus facilisis vulputate. In sed fermentum magna, vitae" +
                " molestie justo.Pellentesque mollis eleifend varius. Suspendisse fringilla erat a porttitor" +
                " varius. Praesent tempus, purus vitae vehicula rhoncus, arcu lorem egestas lorem, vel elementum" +
                " elit dolor eget elit. Cras ac accumsan libero. Nullam commodo iaculis risus, sit amet dictum eros" +
                " viverra eu. Aliquam malesuada est eu lorem imperdiet dapibus. Morbi auctor nibh a ante feugiat," +
                " id tempor mauris tincidunt. Ut vulputate venenatis erat nec congue. Sed vulputate ligula et" +
                " maximus volutpat. Sed ac convallis metus. Cum sociis natoque penatibus et magnis dis parturient" +
                " montes, nascetur ridiculus mus. Sed velit nisl, faucibus eget massa vitae, pulvinar imperdiet" +
                " erat. Donec bibendum, eros et semper efficitur, mi augue elementum dui, consequat porttitor ipsum" +
                " nisl quis dolor. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac" +
                " turpis egestas.In lacinia et magna sed interdum. Mauris feugiat ornare mi, ultrices porta velit" +
                " dictum non. Nullam venenatis erat eget nisl efficitur scelerisque id eget ipsum. Vivamus" +
                " consequat, neque sed efficitur pharetra, orci lorem mollis nunc, eget maximus mauris erat eu" +
                " purus. Nulla tincidunt velit ut lorem varius vestibulum. Duis euismod, nisi ullamcorper auctor" +
                " efficitur, lectus sapien imperdiet felis, ut pulvinar mauris diam sit amet risus. Donec" +
                " sollicitudin nec nibh ac egestas. Donec eleifend velit diam, at maximus orci bibendum dapibus." +
                " Sed sagittis fermentum ex, ac rutrum justo volutpat porta. Aliquam elementum lorem sed dapibus" +
                " scelerisque. Aenean semper tristique diam non malesuada. Proin finibus imperdiet suscipit." +
                "Pellentesque faucibus eros vulputate sem rhoncus ullamcorper. Maecenas a velit et dui lobortis" +
                " fermentum ac in augue. Sed non diam odio. Praesent arcu ligula, bibendum eget volutpat ut," +
                " tincidunt a augue. Proin euismod tristique eleifend. Aliquam porta accumsan turpis, nec vulputate" +
                " sem egestas pharetra. Donec feugiat tortor ante, vel tristique mauris facilisis at. Quisque" +
                " aliquam interdum eros, dapibus hendrerit est interdum in. Donec turpis nisl, tempus sed ex vitae," +
                " dictum vehicula quam. Etiam ac dui laoreet, dictum nunc at, facilisis est. Mauris rhoncus justo" +
                " vitae magna accumsan pulvinar.Sed imperdiet sed dui in pulvinar. Etiam ipsum neque, dapibus sit" +
                " amet laoreet commodo, pulvinar sed erat. Nam blandit lobortis erat, non rhoncus libero" +
                " condimentum euismod. Nunc semper, elit nec pellentesque sagittis, metus velit sagittis ex," +
                " sed auctor erat eros quis purus. Fusce tempus ornare velit quis tempor. Aliquam ac dictum" +
                " odio. Nullam in tincidunt justo. Vivamus fermentum vestibulum facilisis. In ut leo eget felis" +
                " aliquet tincidunt at ac magna. Vivamus nunc risus, elementum eu pretium non, venenatis et sem." +
                " Sed ornare ultrices nulla ut iaculis. Duis tempor justo quis libero congue feugiat. Quisque" +
                " commodo vestibulum eros, a cursus lectus. Donec a euismod lorem. Integer feugiat vestibulum dui," +
                " sed aliquet lorem semper nec. Integer nec libero quis ex aliquam luctus at nec ligula.Nam a purus" +
                " eget eros bibendum placerat imperdiet at nisl. Interdum et malesuada fames ac ante ipsum primis" +
                " in faucibus. Proin posuere sapien eu suscipit vehicula. Integer blandit mauris et justo congue" +
                " aliquet. Etiam varius augue nulla, sed posuere ipsum tincidunt id. Lorem ipsum dolor sit amet," +
                " consectetur adipiscing elit. Donec eu ultrices augue, id consequat nibh. Nam blandit libero non" +
                " lectus sollicitudin, id placerat dui elementum. Mauris eu tellus ac est sagittis sodales. Donec" +
                " mollis tempor leo, vitae viverra dui vulputate porttitor. Nulla facilisi. Fusce venenatis ac" +
                " nisi sit amet ullamcorper. Vestibulum quis risus tempus erat gravida suscipit.Praesent volutpat" +
                " luctus nulla eu ultrices. Quisque porta gravida turpis, vel fringilla turpis elementum in. Ut id" +
                " ultrices dui. Proin ante turpis, feugiat sed porttitor sit amet, rhoncus vel arcu. Sed mi turpis," +
                " volutpat vel iaculis ut, tempus et massa. Fusce et efficitur augue. Nullam id risus placerat," +
                " dapibus dolor at, tempor augue. Donec laoreet sed ante ac malesuada.Aenean sollicitudin purus" +
                " lorem, id ultricies nibh mollis eget. Ut ullamcorper nisl libero. Mauris eu ex nulla. Maecenas" +
                " est arcu, dapibus id elit a, dapibus tristique justo. Donec at lectus consequat, bibendum dui" +
                " ac, facilisis ligula. Aenean ornare, mi et blandit consectetur, elit felis blandit nisl, vitae" +
                " eleifend tellus lacus ac est. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices" +
                " posuere cubilia Curae; Aliquam erat volutpat. Praesent elit elit, pharetra a tempus egestas," +
                " elementum consectetur dolor. Ut sed magna tincidunt, tempor nulla commodo, cursus risus. Aenean" +
                " quis arcu metus. Quisque ut massa mi. Nullam tempus aliquam orci quis laoreet. Sed leo eros," +
                " maximus a dolor quis, aliquam hendrerit mauris. Nunc aliquam tortor sit amet ligula blandit" +
                " ornare. Mauris suscipit venenatis mi.Interdum et malesuada fames ac ante ipsum primis in" +
                " faucibus. Integer quis arcu sit amet sem condimentum eleifend fringilla pharetra diam. Ut nec" +
                " est tortor. Mauris dictum diam sit amet enim auctor efficitur. Aliquam commodo dolor lacinia," +
                " dictum lacus ac, feugiat odio. Vestibulum dapibus ante eget lectus molestie, quis accumsan" +
                " tortor viverra. Duis egestas auctor ante, quis ullamcorper velit euismod ac. Nullam ultrices" +
                " convallis sem, ut malesuada nibh euismod vel. Praesent leo nisi, feugiat et odio sit amet, mattis" +
                " finibus nibh.Donec ornare pulvinar vestibulum. Aliquam nunc ante, interdum maximus metus ac," +
                " cursus rutrum sem. Quisque at arcu in velit imperdiet ultrices nec quis nisl. Vestibulum faucibus," +
                " dolor eget placerat condimentum, magna lorem varius elit, et ornare ipsum nisl tristique mauris." +
                " Morbi sodales tempus mattis. Praesent consequat neque justo, non consectetur mauris sodales quis." +
                " Sed in lorem dapibus, facilisis lacus in, eleifend tellus. Quisque bibendum et mauris in" +
                " scelerisque. Phasellus quis aliquam tortor. Maecenas id finibus purus. Duis sit amet erat" +
                " efficitur, sagittis eros non, auctor dolor. Vivamus enim arcu, mattis ac sagittis facilisis," +
                " auctor eget justo. Vivamus tristique ipsum sollicitudin, condimentum turpis eget, molestie sed..";
    }
}
