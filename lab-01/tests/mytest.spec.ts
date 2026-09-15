import { test, expect } from '@playwright/test';

const BASE_URL = 'https://www.saucedemo.com/';

test('successful login', async ({ page }) => {
  // Open SauceDemo and enter valid login credentials.
  await page.goto(BASE_URL);

  await page.getByPlaceholder('Username').fill('standard_user');
  await page.getByPlaceholder('Password').fill('secret_sauce');
  await page.getByRole('button', { name: 'Login' }).click();

  // Verify that login was successful.
  await expect(page).toHaveURL(/inventory/);
  await expect(page.getByText('Products', { exact: true })).toBeVisible();

  // Logout so the test finishes cleanly.
  await page.getByRole('button', { name: 'Open Menu' }).click();
  await page.getByRole('link', { name: 'Logout' }).click();
});

test('failed login with wrong password', async ({ page }) => {
  // Attempt to login using an incorrect password.
  await page.goto(BASE_URL);

  await page.getByPlaceholder('Username').fill('standard_user');
  await page.getByPlaceholder('Password').fill('wrong_password');
  await page.getByRole('button', { name: 'Login' }).click();

  // Verify that the expected error message is shown.
  await expect(
    page.getByText(/Username and password do not match/i)
  ).toBeVisible();
});

test('add product to cart after login', async ({ page }) => {
  // Login first.
  await page.goto(BASE_URL);

  await page.getByPlaceholder('Username').fill('standard_user');
  await page.getByPlaceholder('Password').fill('secret_sauce');
  await page.getByRole('button', { name: 'Login' }).click();

  // Add the first available product to the cart.
  await page.getByRole('button', { name: 'Add to cart' }).first().click();

  // Open the shopping cart.
  await page.locator('.shopping_cart_link').click();

  // Verify that exactly one product is in the cart.
  await expect(page.locator('.cart_item')).toHaveCount(1);

  // Logout after completing the test.
  await page.getByRole('button', { name: 'Open Menu' }).click();
  await page.getByRole('link', { name: 'Logout' }).click();
});
