#ifndef MAINWINDOW_HPP
#define MAINWINDOW_HPP

#include <SFML/Graphics.hpp>
#include <memory>
#include "FormWithProxy.hpp"
#include "FormWithoutProxy.hpp"

class MainWindow
{
private:
    sf::RenderWindow& window;
    sf::Font font;

    sf::RectangleShape btnWithProxy;
    sf::RectangleShape btnWithoutProxy;
    sf::RectangleShape btnExit;

    std::unique_ptr<sf::Text> titleText;
    std::unique_ptr<sf::Text> descText;

    bool isWithProxyOpen = false;
    bool isWithoutProxyOpen = false;

    std::unique_ptr<FormWithProxy> formWithProxy;
    std::unique_ptr<FormWithoutProxy> formWithoutProxy;

    void initUI();

public:
    MainWindow(sf::RenderWindow& wnd);

    void handleEvent(const sf::Event& event);
    void draw(sf::RenderWindow& window);
};

#endif // MAINWINDOW_HPP