#ifndef FORMWITHPROXY_HPP
#define FORMWITHPROXY_HPP

#include <SFML/Graphics.hpp>
#include <vector>
#include <memory>
#include "ProxyDocument.hpp"

class FormWithProxy
{
private:
    sf::RenderWindow& window;
    sf::Font& font;

    std::vector<std::shared_ptr<IDocument>> documents;

    sf::RectangleShape btnShowPreview;
    sf::RectangleShape btnLoadFirst;
    sf::RectangleShape btnLoadAll;
    sf::RectangleShape btnRepeatAccess;
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
    FormWithProxy(sf::RenderWindow& wnd, sf::Font& fnt);
    ~FormWithProxy();

    void handleEvent(const sf::Event& event);
    void draw(sf::RenderWindow& window);
    bool getIsOpen() const { return isOpen; }
    void close() { isOpen = false; }
};

#endif // FORMWITHPROXY_HPP