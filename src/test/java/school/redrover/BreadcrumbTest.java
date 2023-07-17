package school.redrover;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import school.redrover.model.*;
import school.redrover.model.base.BaseSubmenuPage;
import school.redrover.model.jobs.FreestyleProjectPage;
import school.redrover.model.jobsconfig.FreestyleProjectConfigPage;
import school.redrover.runner.BaseTest;
import school.redrover.runner.TestUtils;

import java.util.Arrays;
import java.util.List;

public class BreadcrumbTest extends BaseTest {
    @Test
    public void testNavigateToManageJenkinsFromDropDown() {
        String actualResult = new MainPage(getDriver())
                .getBreadcrumb()
                .getPageFromDashboardDropdownMenu("Manage Jenkins", new ManageJenkinsPage(getDriver()))
                .getActualHeader();

        Assert.assertEquals(actualResult, "Manage Jenkins");
    }

    @DataProvider(name = "subsections")
    public Object[][] provideSubsection() {
        return new Object[][]{
                {new ConfigureSystemPage(getDriver())},
                {new GlobalToolConfigurationPage(getDriver())},
                {new PluginsPage(getDriver())},
                {new ManageNodesPage(getDriver())},
                {new ConfigureGlobalSecurityPage(getDriver())},
                {new CredentialsPage(getDriver())},
                {new ConfigureCredentialProvidersPage(getDriver())},
                {new UserPage(getDriver())},
                {new SystemInformationPage(getDriver())},
                {new LogRecordersPage(getDriver())},
                {new LoadStatisticsPage(getDriver())},
                {new AboutJenkinsPage(getDriver())},
                {new ManageOldDataPage(getDriver())},
                {new JenkinsCLIPage(getDriver())},
                {new ScriptConsolePage(getDriver())},
                {new PrepareForShutdownPage(getDriver())}
        };
    }

    @Test(dataProvider = "subsections")
    public <PageFromSubMenu extends BaseSubmenuPage<PageFromSubMenu>> void testNavigateToManageJenkinsSubsection(PageFromSubMenu pageFromSubMenu) {
        new MainPage(getDriver())
                .getBreadcrumb()
                .getDashboardDropdownMenu()
                .selectAnOptionFromDashboardManageJenkinsSubmenuList(pageFromSubMenu);

        String pageName = getDriver().findElement(By.xpath("//h1")).getText();

        switch (pageFromSubMenu.callByMenuItemName()) {
            case "System Log" -> Assert.assertEquals(pageName, "Log Recorders");
            case "Load Statistics" -> Assert.assertTrue(pageName.toLowerCase().contains(pageFromSubMenu.callByMenuItemName().toLowerCase()));
            case "About Jenkins" -> Assert.assertTrue(pageName.contains("Jenkins\n" + "Version"));
            default -> Assert.assertTrue(pageFromSubMenu.callByMenuItemName().toLowerCase().contains(pageName.toLowerCase()));
        }
    }

    @Ignore
    @Test
    public void testReloadConfigurationFromDiskOfManageJenkinsSubmenu() {
        String expectedLoadingText = "Please wait while Jenkins is getting ready to work ...";

        new MainPage(getDriver())
                .getBreadcrumb()
                .getDashboardDropdownMenu()
                .selectAnOptionFromDashboardManageJenkinsSubmenuList(new ConfigureSystemPage(getDriver()))
                .getBreadcrumb()
                .clickOkOnPopUp();

        String loadingText = getDriver().findElement(By.xpath("//h1")).getText();

        Assert.assertEquals(loadingText, expectedLoadingText);
    }

    @Test
    public void testDashboardDropdownMenu() {
        final List<String> expectedMenuList = Arrays.asList("New Item", "People", "Build History", "Manage Jenkins", "My Views");

        List<String> actualMenuList = new MainPage(getDriver())
                .getBreadcrumb()
                .getDashboardDropdownMenu()
                .getMenuList();

        Assert.assertEquals(actualMenuList, expectedMenuList);
    }

    @Test
    public void testReturnToDashboardPageFromProjectPage() {
        final String nameProject = "One";

        String nameProjectOnMainPage = new MainPage(getDriver())
                .clickNewItem()
                .enterItemName(nameProject)
                .selectJobType(TestUtils.JobType.FreestyleProject)
                .clickOkButton(new FreestyleProjectConfigPage(new FreestyleProjectPage(getDriver())))
                .clickSaveButton()
                .getBreadcrumb()
                .clickDashboardButton()
                .getJobName(nameProject);

        Assert.assertEquals(nameProjectOnMainPage, nameProject);
    }


    @Test
    public void testNavigateToPluginsPageFromPeoplePage() {
        String actualTitle = new MainPage(getDriver())
                .clickPeopleOnLeftSideMenu()
                .getBreadcrumb()
                .selectAnOptionFromDashboardManageJenkinsSubmenuList(new PluginsPage(getDriver()))
                .getPageTitle();

        Assert.assertEquals(actualTitle, "Plugins");
    }

    @Test
    public void testNavigateToPluginsPageFromDropDown() {
        String actualResult = new MainPage(getDriver())
                .getBreadcrumb()
                .selectAnOptionFromDashboardManageJenkinsSubmenuList(new PluginsPage(getDriver()))
                .getPageTitle();

        Assert.assertEquals(actualResult, "Plugins");
    }

    @Test
    public void testNavigateToPeoplePageFromBuildHistoryPage() {
        String actualTitle = new MainPage(getDriver())
                .clickBuildsHistoryButton()
                .getBreadcrumb()
                .getPageFromDashboardDropdownMenu("People", new PeoplePage(getDriver()))
                .getPageTitle();

        Assert.assertEquals(actualTitle, "People");
    }
}
