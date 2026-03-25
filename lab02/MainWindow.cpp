#include "MainWindow.hpp"
#include <iostream>

MainWindow::MainWindow(sf::RenderWindow& wnd)
    : window(wnd)
{
    if (!font.openFromFile("C:/Windows/Fonts/arial.ttf"))
    {
        std::cerr << "Failed to load font!" << std::endl;
    }

    initUI();
}

void MainWindow::initUI()
{
    // Создаём текстовые объекты через make_unique
    titleText = std::make_unique<sf::Text>();
    descText = std::make_unique<sf::Text>();

    // Заголовок
    titleText->setFont(font);
    titleText->setString("Proxy Pattern Demo");
    titleText->setCharacterSize(24);
    titleText->setFillColor(sf::Color::Black);
    titleText->setPosition(sf::Vector2f(250.0f, 50.0f));

    // Описание
    descText->setFont(font);
    descText->setString("Choose mode:\n\n"
        "With Proxy - lazy loading\n"
        "Without Proxy - immediate loading");
    descText->setCharacterSize(16);
    descText->setFillColor(sf::Color::Black);
    descText->setPosition(sf::Vector2f(250.0f, 100.0f));

    // Кнопка "С паттерном"
    btnWithProxy.setSize(sf::Vector2f(400.0f, 60.0f));
    btnWithProxy.setPosition(sf::Vector2f(200.0f, 250.0f));
    btnWithProxy.setFillColor(sf::Color(144, 238, 144));

    // Кнопка "Без паттерна"
    btnWithoutProxy.setSize(sf::Vector2f(400.0f, 60.0f));
    btnWithoutProxy.setPosition(sf::Vector2f(200.0f, 330.0f));
    btnWithoutProxy.setFillColor(sf::Color(255, 165, 0));

    // Кнопка "Выход"
    btnExit.setSize(sf::Vector2f(400.0f, 50.0f));
    btnExit.setPosition(sf::Vector2f(200.0f, 420.0f));
    btnExit.setFillColor(sf::Color(240, 128, 128));
}

void MainWindow::handleEvent(const sf::Event& event)
{
    if (event.is<sf::Event::MouseButtonPressed>())
    {
        sf::Vector2i mousePosInt = sf::Mouse::getPosition(window);
        sf::Vector2f mousePos = window.mapPixelToCoords(mousePosInt);

        // Кнопка "С паттерном"
        if (btnWithProxy.getGlobalBounds().contains(mousePos))
        {
            if (!isWithProxyOpen)
            {
                formWithProxy = std::make_unique<FormWithProxy>(window, font);
                isWithProxyOpen = true;
            }
        }
        // Кнопка "Без паттерна"
        else if (btnWithoutProxy.getGlobalBounds().contains(mousePos))
        {
            if (!isWithoutProxyOpen)
            {
                formWithoutProxy = std::make_unique<FormWithoutProxy>(window, font);
                isWithoutProxyOpen = true;
            }
        }
        // Кнопка "Выход"
        else if (btnExit.getGlobalBounds().contains(mousePos))
        {
            window.close();
        }
    }

    // Обработка событий открытых форм
    if (isWithProxyOpen && formWithProxy)
    {
        formWithProxy->handleEvent(event);
        if (!formWithProxy->getIsOpen())
        {
            isWithProxyOpen = false;
            formWithProxy.reset();
        }
    }

    if (isWithoutProxyOpen && formWithoutProxy)
    {
        formWithoutProxy->handleEvent(event);
        if (!formWithoutProxy->getIsOpen())
        {
            isWithoutProxyOpen = false;
            formWithoutProxy.reset();
        }
    }
}

void MainWindow::draw(sf::RenderWindow& window)
{
    if (titleText)
        window.draw(*titleText);
    if (descText)
        window.draw(*descText);
    window.draw(btnWithProxy);
    window.draw(btnWithoutProxy);
    window.draw(btnExit);

    // Рисуем активные формы
    if (isWithProxyOpen && formWithProxy)
        formWithProxy->draw(window);

    if (isWithoutProxyOpen && formWithoutProxy)
        formWithoutProxy->draw(window);
}