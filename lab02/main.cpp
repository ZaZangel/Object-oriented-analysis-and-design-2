#include <SFML/Graphics.hpp>
#include "MainWindow.hpp"

int main()
{
    // ? SFML 3.x: VideoMode с фигурными скобками
    sf::RenderWindow window(sf::VideoMode({800, 600}), "Proxy Pattern - SFML Demo");
    window.setFramerateLimit(60);
    
    MainWindow mainWindow(window);
    
    while (window.isOpen())
    {
        // ? SFML 3.x: pollEvent возвращает std::optional
        if (auto event = window.pollEvent())
        {
            if (event->is<sf::Event::Closed>())
            {
                window.close();
            }
            
            mainWindow.handleEvent(*event);
        }
        
        window.clear(sf::Color(50, 50, 50));
        mainWindow.draw(window);
        window.display();
    }
    
    return 0;
}