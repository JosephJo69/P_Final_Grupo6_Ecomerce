package gt.edu.umg.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tree.engine.CollectionsTreeStrategy;
import org.tree.engine.CustomTreeImpl;
import org.tree.engine.model.TreeAlgorithmStrategy;

@Configuration
public class TreeStrategyConfig {

    @Bean
    @ConditionalOnProperty(name = "app.tree-strategy", havingValue = "custom", matchIfMissing = true)
    public TreeAlgorithmStrategy customTreeStrategy() {
        return new CustomTreeImpl();
    }

    @Bean
    @ConditionalOnProperty(name = "app.tree-strategy", havingValue = "collections")
    public TreeAlgorithmStrategy collectionsTreeStrategy() {
        return new CollectionsTreeStrategy();
    }
}
