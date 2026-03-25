#ifndef FORMWITHOUTPROXY_HPP
#define FORMWITHOUTPROXY_HPP

#include <SFML/Graphics.hpp>
#include <vector>
#include <memory>
#include "RealDocument.hpp"

class FormWithoutProxy
{
private:
    sf::RenderWindow& window;
    sf::Font& font;

    std::vector<std::shared_ptr<RealDocument>> documents;

    sf::RectangleShape btnShowInfo;
    sf::RectangleShape btnDisplayFirst;
    sf::RectangleShape btnDisplayAll;
    sf::RectangleShape btnClearLog;
    sf::RectangleShape btnClose;

    std::unique_ptr<sf::Text> titleText;
    std::unique_ptr<sf::Text> logText;
    std::string logContent;

    bool isOpen = true;

    void initUI();
    void initializeDocuments();
    void addToLog(const std::string& message);

public:
    FormWithoutProxy(sf::RenderWindow& wnd, sf::Font& fnt);
    ~FormWithoutProxy();

    void handleEvent(const sf::Event& event);
    void draw(sf::RenderWindow& window);
    bool getIsOpen() const { return isOpen; }
    void close() { isOpen = false; }
};

#endif // FORMWITHOUTPROXY_HPP