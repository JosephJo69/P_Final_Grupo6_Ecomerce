package gt.edu.umg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tree.engine.CollectionsTreeStrategy;
import org.tree.engine.CustomTreeImpl;
import org.tree.engine.model.TreeAlgorithmStrategy;

@Configuration
public class TreeStrategyConfig {

    @Bean
    public TreeAlgorithmStrategy treeAlgorithmStrategy(
            @Value("${app.tree-strategy:custom}") String strategy
    ) {
        if ("collections".equalsIgnoreCase(strategy.trim())) {
            return new CollectionsTreeStrategy();
        }
        return new CustomTreeImpl();
    }
}
