package automationexercises.utils.actions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class PageActions {

    private final Page page;

    public PageActions(Page page) {
        this.page = page;
    }


    public Locator find(String selector) {
        return page.locator(selector);
    }

    public Locator findByText(String text) {
        return page.getByText(text);
    }

    public Locator findByLabel(String label) {
        return page.getByLabel(label);
    }

    public Locator findByRole(AriaRole role, String name) {
        return page.getByRole(role, new Page.GetByRoleOptions().setName(name));
    }

    public Locator findByPlaceholder(String placeholder) {
        return page.getByPlaceholder(placeholder);
    }

}
